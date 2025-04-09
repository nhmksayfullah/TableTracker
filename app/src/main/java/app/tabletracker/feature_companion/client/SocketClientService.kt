package app.tabletracker.feature_companion.client

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
import app.tabletracker.auth.domain.repository.AuthRepository
import app.tabletracker.feature_companion.model.ACTION_REQUEST_SERVER_CONNECTION
import app.tabletracker.feature_companion.model.ACTION_SERVER_CONNECTION_AVAILABLE
import app.tabletracker.feature_companion.model.ClientRequest
import app.tabletracker.feature_companion.model.EXTRA_SERVER_ADDRESS
import app.tabletracker.feature_companion.model.EXTRA_SERVER_CONNECTION
import app.tabletracker.feature_companion.model.ServerResponse
import app.tabletracker.feature_menu.domain.repository.EditMenuRepository
import app.tabletracker.util.TableTracker
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
        val app = applicationContext as TableTracker
        authRepository = app.container.authRepository
        editMenuRepository = app.container.editMenuRepository
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
                CoroutineScope(Dispatchers.IO).launch {
                    socketClientManager.transmitDataToServer(
                        Json.encodeToString(ClientRequest.serializer(),
                            ClientRequest.SetupRestaurant
                        )
                    )
                }
            }
            ClientAction.RequestMenu.toString() -> {
                CoroutineScope(Dispatchers.IO).launch {
                    socketClientManager.transmitDataToServer(
                        Json.encodeToString(ClientRequest.serializer(),
                            ClientRequest.SyncMenu)
                    )
                }
            }
            ClientAction.RequestOrderInfo.toString() -> {
            }
            ClientAction.SendOrderInfo.toString() -> {

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
                    response.menu.forEach { categoryWithMenuItems ->
                        editMenuRepository.writeCategory(categoryWithMenuItems.category)
                        categoryWithMenuItems.menuItems.forEach { menuItem ->
                            editMenuRepository.writeMenuItem(menuItem)
                        }
                    }
                }
            }
            is ServerResponse.OrderInfo -> {}
            is ServerResponse.RestaurantInfo -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val restaurant = response.restaurant
                    val restaurantExtra = response.restaurantExtra
                    authRepository.registerRestaurant(restaurant)
                    authRepository.upsertRestaurantExtra(restaurantExtra)
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