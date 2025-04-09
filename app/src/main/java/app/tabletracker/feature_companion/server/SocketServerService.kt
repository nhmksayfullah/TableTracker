package app.tabletracker.feature_companion.server

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import app.tabletracker.R
import app.tabletracker.feature_companion.model.ClientRequest
import app.tabletracker.feature_companion.model.ServerAction
import app.tabletracker.feature_order.domain.repository.OrderRepository
import app.tabletracker.feature_printing.domain.PrinterManager
import app.tabletracker.util.TableTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SocketServerService: Service() {

    private lateinit var socketServerManager: SocketServerManager
    private lateinit var orderRepository: OrderRepository
    private lateinit var printerManager: PrinterManager
    private val serverState = MutableStateFlow(ServerState())

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(
                addressRequestReceiver,
                IntentFilter(ACTION_REQUEST_SERVER_ADDRESS),
                RECEIVER_NOT_EXPORTED
            )
        }

        val app = applicationContext as TableTracker
        orderRepository = app.container.orderRepository
        printerManager = PrinterManager(this)
        socketServerManager = SocketServerManagerImpl(
            context = this,
            onRequestReceived = ::handleClientRequest
        )
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when(intent?.action) {
            ServerAction.Start.toString() -> {
                startService()
            }
            ServerAction.Stop.toString() -> {
                CoroutineScope(Dispatchers.IO).launch {
                    socketServerManager.stopServer()
                    stopSelf()
                }
            }
        }
        return START_STICKY
    }
    private fun startService() {
        CoroutineScope(Dispatchers.IO).launch {
            socketServerManager.startServer()
        }
        socketServerManager.observeServerState().onEach { newServerState ->

            sendBroadcast(newServerState)
            createNotification(newServerState)

            serverState.update {
                it.copy(
                    isRunning = newServerState.isRunning,
                    ipAddress = newServerState.ipAddress,
                    port = newServerState.port,
                    connectedClients = newServerState.connectedClients
                )
            }
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }

    private fun sendBroadcast(state: ServerState) {
        if (state.ipAddress != null) {
            val intent = Intent(ACTION_SERVER_ADDRESS_AVAILABLE).apply {
                putExtra(EXTRA_SERVER_ADDRESS, "${state.ipAddress}:${state.port}")
            }
            sendBroadcast(intent)
        }
        if (state.connectedClients.size > serverState.value.connectedClients.size) {
            val intent = Intent(ACTION_CLIENT_CONNECTED)
            sendBroadcast(intent)
        }
    }

    private fun createNotification(state: ServerState) {
        if (state.isRunning) {
            val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Connection at ${state.ipAddress}:${state.port}")
                .setContentText("Listening for new orders")
                .build()
            startForeground(1, notification)
        } else {
            val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Connecting to Companion Device")
                .setContentText("Please wait...")
                .build()
            startForeground(1, notification)
        }
    }

    private fun handleClientRequest(request: ClientRequest) {
        when(request) {
            ClientRequest.SetupRestaurant -> {
                orderRepository.readRestaurantInfo().onEach {restaurantInfo ->
                    val responseJson = Json.encodeToString(restaurantInfo)
                    TODO("Send response to client")
                }.launchIn(CoroutineScope(Dispatchers.IO))
            }
            ClientRequest.SyncMenu -> {
                orderRepository.readAllCategoriesWithMenuItems().onEach { menu ->
                    val responseJson = Json.encodeToString(menu)
                    TODO("Send response to client")
                }.launchIn(CoroutineScope(Dispatchers.IO))
            }
            is ClientRequest.IncomingOrder -> TODO()
            is ClientRequest.SyncOrder -> TODO()
        }
    }


    private val addressRequestReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_REQUEST_SERVER_ADDRESS) {
                val address = "${serverState.value.ipAddress}:${serverState.value.port}"
                val reply = Intent(ACTION_SERVER_ADDRESS_AVAILABLE).apply {
                    putExtra(EXTRA_SERVER_ADDRESS, address)
                }
                sendBroadcast(reply)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(addressRequestReceiver)
        CoroutineScope(Dispatchers.IO).launch {
            socketServerManager.stopServer()
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    companion object {
        const val NOTIFICATION_CHANNEL = "SocketServerService"
    }
}