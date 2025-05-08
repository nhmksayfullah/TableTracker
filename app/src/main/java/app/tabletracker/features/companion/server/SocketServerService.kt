package app.tabletracker.features.companion.server

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
import app.tabletracker.features.companion.model.ACTION_CLIENT_CONNECTED
import app.tabletracker.features.companion.model.ACTION_REQUEST_SERVER_ADDRESS
import app.tabletracker.features.companion.model.ACTION_SERVER_ADDRESS_AVAILABLE
import app.tabletracker.features.companion.model.ClientRequest
import app.tabletracker.features.companion.model.EXTRA_SERVER_ADDRESS
import app.tabletracker.features.companion.model.ServerResponse
import app.tabletracker.features.order.domain.repository.OrderRepository
import app.tabletracker.features.printing.domain.UsbPrinterManager
import app.tabletracker.features.receipt.domain.ReceiptGenerator
import app.tabletracker.app.TableTrackerApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class SocketServerService : Service() {

    private lateinit var socketServerManager: SocketServerManager
    private lateinit var orderRepository: OrderRepository
    private lateinit var printerManager: UsbPrinterManager
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

        val app = applicationContext as TableTrackerApplication
        orderRepository = app.container.orderRepository
        printerManager = UsbPrinterManager(this)
        socketServerManager = SocketServerManagerImpl(
            context = this,
            onRequestReceived = ::handleClientRequest
        )
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
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
            Log.d("SocketServerService", "Auto sendBroadcast: ${state.ipAddress}:${state.port}")
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
        Log.d("SocketServerService", "handleClientRequest: $request")
        when (request) {
            ClientRequest.SetupRestaurant -> {
                orderRepository.readRestaurantInfo().onEach { restaurantInfo ->
                    orderRepository.readRestaurantExtra(restaurantInfo.id).onEach { restaurantExtra ->
                        val response = ServerResponse.RestaurantInfo(restaurantInfo, restaurantExtra)
                        val responseJson = Json.encodeToString(ServerResponse.serializer(), response)
                        socketServerManager.transmitDataToClient(
                            serverState.value.connectedClients.first(),
                            responseJson
                        )
                    }.launchIn(CoroutineScope(Dispatchers.IO))
                }.launchIn(CoroutineScope(Dispatchers.IO))
            }

            ClientRequest.SyncMenu -> {
                orderRepository.readAllCategoriesWithMenuItems().onEach { menu ->
                    val response = ServerResponse.Menu(menu)
                    val responseJson = Json.encodeToString(ServerResponse.serializer(), response)
                    socketServerManager.transmitDataToClient(
                        serverState.value.connectedClients.first(),
                        responseJson
                    )
                }.launchIn(CoroutineScope(Dispatchers.IO))
            }

            is ClientRequest.IncomingOrder -> {
                val orderWithOrderItems = request.orderWithOrderItems
                CoroutineScope(Dispatchers.IO).launch {
                    // Save the incoming order to the database
                    orderRepository.writeOrder(orderWithOrderItems.order)
                    orderWithOrderItems.orderItems.forEach { orderItem ->
                        orderRepository.writeOrderItem(orderItem)
                    }

                    // Get restaurant info for receipt generation
                    orderRepository.readRestaurantInfo().collect { restaurantInfo ->
                        // Get the order with its items
                        orderRepository.readOrderWithOrderItems(orderWithOrderItems.order.id).collect { orderWithItems ->
                            // Generate and print receipt
                            val receiptGenerator = ReceiptGenerator(restaurantInfo, orderWithItems)

                            // Print kitchen copy
                            val kitchenCopy = receiptGenerator.generateKitchenCopy()
                            printerManager.print(kitchenCopy)

                            // Print customer receipt
                            val receipt = receiptGenerator.generateReceipt()
                            printerManager.print(receipt)
                        }
                    }
                }
            }
            is ClientRequest.SyncOrder -> {
                val orderId = request.orderId
                orderRepository.readOrderWithOrderItems(orderId).onEach { orderWithItems ->
                    val response = ServerResponse.OrderInfo(orderWithItems.order)
                    val responseJson = Json.encodeToString(ServerResponse.serializer(), response)
                    socketServerManager.transmitDataToClient(
                        serverState.value.connectedClients.first(),
                        responseJson
                    )
                }.launchIn(CoroutineScope(Dispatchers.IO))
            }
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
                Log.d("SocketServerService", "Upon request: onReceive: $address")
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
