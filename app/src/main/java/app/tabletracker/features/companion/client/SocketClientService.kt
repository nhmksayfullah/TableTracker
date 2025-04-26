package app.tabletracker.features.companion.client

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import app.tabletracker.R
import app.tabletracker.features.auth.domain.repository.AuthRepository
import app.tabletracker.features.companion.model.ACTION_REQUEST_SERVER_CONNECTION
import app.tabletracker.features.companion.model.ACTION_SERVER_CONNECTION_AVAILABLE
import app.tabletracker.features.companion.model.ACTION_SYNC_RESTAURANT_INFO_STATUS
import app.tabletracker.features.companion.model.ACTION_SYNC_MENU_STATUS
import app.tabletracker.features.companion.model.ClientRequest
import app.tabletracker.features.companion.model.EXTRA_SERVER_ADDRESS
import app.tabletracker.features.companion.model.EXTRA_SERVER_CONNECTION
import app.tabletracker.features.companion.model.EXTRA_SYNC_STATUS
import app.tabletracker.features.companion.model.EXTRA_SYNC_MESSAGE
import app.tabletracker.features.companion.model.ServerResponse
import app.tabletracker.features.companion.model.SYNC_STATUS_COMPLETED
import app.tabletracker.features.companion.model.SYNC_STATUS_FAILED
import app.tabletracker.features.companion.model.SYNC_STATUS_IN_PROGRESS
import app.tabletracker.features.inventory.domain.repository.EditMenuRepository
import app.tabletracker.features.order.domain.repository.OrderRepository
import app.tabletracker.app.TableTrackerApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class SocketClientService: Service() {

    private lateinit var socketClientManager: SocketClientManager
    private lateinit var authRepository: AuthRepository
    private lateinit var editMenuRepository: EditMenuRepository
    private lateinit var orderRepository: OrderRepository
    private val clientState = MutableStateFlow(ClientState())

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(
                serverConnectedRequestReceiver,
                IntentFilter(ACTION_REQUEST_SERVER_CONNECTION),
                RECEIVER_NOT_EXPORTED
            )
        }
        val app = applicationContext as TableTrackerApplication
        authRepository = app.container.authRepository
        editMenuRepository = app.container.editMenuRepository
        orderRepository = app.container.orderRepository
        socketClientManager = SocketClientManagerImpl(
            onResponseReceived = ::handleServerResponse
        )

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ClientAction.Connect.toString() -> {
                val serverAddress = intent.getStringExtra(EXTRA_SERVER_ADDRESS)
                Log.d("SocketClientService", "onStartCommand: $serverAddress")
                if (serverAddress != null) {
                    try {
                        connectToServer(serverAddress)
                    } catch (e: Exception) {}
                }
            }
            ClientAction.Disconnect.toString() -> {
                CoroutineScope(Dispatchers.IO).launch {
                    socketClientManager.disconnectFromServer()
                    stopSelf()
                }
            }
            ClientAction.RequestRestaurantInfo.toString() -> {
                // Broadcast sync in progress status
                val syncInProgressIntent = Intent(ACTION_SYNC_RESTAURANT_INFO_STATUS).apply {
                    putExtra(EXTRA_SYNC_STATUS, SYNC_STATUS_IN_PROGRESS)
                    putExtra(EXTRA_SYNC_MESSAGE, "Requesting restaurant information...")
                }
                sendBroadcast(syncInProgressIntent)

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        socketClientManager.transmitDataToServer(
                            Json.encodeToString(ClientRequest.serializer(),
                                ClientRequest.SetupRestaurant
                            )
                        )
                    } catch (e: Exception) {
                        // Broadcast sync failed status
                        val syncFailedIntent = Intent(ACTION_SYNC_RESTAURANT_INFO_STATUS).apply {
                            putExtra(EXTRA_SYNC_STATUS, SYNC_STATUS_FAILED)
                            putExtra(EXTRA_SYNC_MESSAGE, "Failed to request restaurant information: ${e.message}")
                        }
                        sendBroadcast(syncFailedIntent)
                    }
                }
            }
            ClientAction.RequestMenu.toString() -> {
                // Broadcast sync in progress status
                val syncInProgressIntent = Intent(ACTION_SYNC_MENU_STATUS).apply {
                    putExtra(EXTRA_SYNC_STATUS, SYNC_STATUS_IN_PROGRESS)
                    putExtra(EXTRA_SYNC_MESSAGE, "Requesting menu information...")
                }
                sendBroadcast(syncInProgressIntent)

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        socketClientManager.transmitDataToServer(
                            Json.encodeToString(ClientRequest.serializer(),
                                ClientRequest.SyncMenu)
                        )
                    } catch (e: Exception) {
                        // Broadcast sync failed status
                        val syncFailedIntent = Intent(ACTION_SYNC_MENU_STATUS).apply {
                            putExtra(EXTRA_SYNC_STATUS, SYNC_STATUS_FAILED)
                            putExtra(EXTRA_SYNC_MESSAGE, "Failed to request menu information: ${e.message}")
                        }
                        sendBroadcast(syncFailedIntent)
                    }
                }
            }
            ClientAction.RequestOrderInfo.toString() -> {
                val orderId = intent.getStringExtra("orderId")
                if (orderId != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        socketClientManager.transmitDataToServer(
                            Json.encodeToString(ClientRequest.serializer(),
                                ClientRequest.SyncOrder(orderId)
                            )
                        )
                    }
                }
            }
            ClientAction.SendOrderInfo.toString() -> {
                val orderId = intent.getStringExtra("orderId")
                if (orderId != null) {
                    orderRepository.readOrderWithOrderItems(orderId).onEach { orderWithItems ->
                        CoroutineScope(Dispatchers.IO).launch {
                            socketClientManager.transmitDataToServer(
                                Json.encodeToString(ClientRequest.serializer(),
                                    ClientRequest.IncomingOrder(orderWithItems)
                                )
                            )
                        }
                    }.launchIn(CoroutineScope(Dispatchers.IO))
                }
            }
        }
        return START_STICKY
    }

    private fun sendBroadcast(state: ClientState) {
        val intent = Intent(ACTION_SERVER_CONNECTION_AVAILABLE).apply {
            putExtra(EXTRA_SERVER_CONNECTION, state.isConnected)
        }
        sendBroadcast(intent)
    }

    private fun createNotification(state: ClientState) {
        if (state.isConnected) {
            val notification = NotificationCompat.Builder(this,
                NOTIFICATION_CHANNEL
            )
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Connection at ${state.serverAddress}")
                .setContentText("Syncing with server...")
                .build()
            startForeground(2, notification)
        } else {
            val notification = NotificationCompat.Builder(this,
                NOTIFICATION_CHANNEL
            )
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Connecting to Main Device")
                .setContentText("Please wait...")
                .build()
            startForeground(2, notification)
        }
    }
    private fun connectToServer(serverAddress: String) {
        val (ipAddress, port) = serverAddress.split(":")
        CoroutineScope(Dispatchers.IO).launch {
            socketClientManager.connectToServer(ipAddress, port.toInt())
        }
        socketClientManager.observeClientState().onEach { newClientState ->
            Log.d("SocketClientService", "connectToServer: $newClientState")
            sendBroadcast(newClientState)
            createNotification(newClientState)
            clientState.update {
                it.copy(
                    isConnected = newClientState.isConnected,
                    serverAddress = newClientState.serverAddress
                )
            }
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }

    private fun handleServerResponse(response: ServerResponse) {
        Log.d("SocketClientService", "handleServerResponse: $response")
        when(response) {
            is ServerResponse.Menu -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        response.menu.forEach { categoryWithMenuItems ->
                            editMenuRepository.writeCategory(categoryWithMenuItems.category)
                            categoryWithMenuItems.menuItems.forEach { menuItem ->
                                editMenuRepository.writeMenuItem(menuItem)
                            }
                        }

                        // Broadcast sync completed status
                        val syncCompletedIntent = Intent(ACTION_SYNC_MENU_STATUS).apply {
                            putExtra(EXTRA_SYNC_STATUS, SYNC_STATUS_COMPLETED)
                            putExtra(EXTRA_SYNC_MESSAGE, "Menu information synced successfully")
                        }
                        sendBroadcast(syncCompletedIntent)
                    } catch (e: Exception) {
                        // Broadcast sync failed status
                        val syncFailedIntent = Intent(ACTION_SYNC_MENU_STATUS).apply {
                            putExtra(EXTRA_SYNC_STATUS, SYNC_STATUS_FAILED)
                            putExtra(EXTRA_SYNC_MESSAGE, "Failed to save menu information: ${e.message}")
                        }
                        sendBroadcast(syncFailedIntent)
                    }
                }
            }
            is ServerResponse.OrderInfo -> {}
            is ServerResponse.RestaurantInfo -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val restaurant = response.restaurant
                        val restaurantExtra = response.restaurantExtra
                        authRepository.registerRestaurant(restaurant)
                        authRepository.upsertRestaurantExtra(restaurantExtra)

                        // Broadcast sync completed status
                        val syncCompletedIntent = Intent(ACTION_SYNC_RESTAURANT_INFO_STATUS).apply {
                            putExtra(EXTRA_SYNC_STATUS, SYNC_STATUS_COMPLETED)
                            putExtra(EXTRA_SYNC_MESSAGE, "Restaurant information synced successfully")
                        }
                        sendBroadcast(syncCompletedIntent)
                    } catch (e: Exception) {
                        // Broadcast sync failed status
                        val syncFailedIntent = Intent(ACTION_SYNC_RESTAURANT_INFO_STATUS).apply {
                            putExtra(EXTRA_SYNC_STATUS, SYNC_STATUS_FAILED)
                            putExtra(EXTRA_SYNC_MESSAGE, "Failed to save restaurant information: ${e.message}")
                        }
                        sendBroadcast(syncFailedIntent)
                    }
                }
            }
        }
    }
    private val serverConnectedRequestReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_REQUEST_SERVER_CONNECTION) {
                val reply = Intent(ACTION_SERVER_CONNECTION_AVAILABLE).apply {
                    putExtra(EXTRA_SERVER_CONNECTION, clientState.value.isConnected)
                }
                sendBroadcast(reply)
                Log.d("SocketClientService", "Upon request: onReceive: ${clientState.value.isConnected}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(serverConnectedRequestReceiver)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    companion object {
        const val NOTIFICATION_CHANNEL = "SocketClientService"
    }
}
