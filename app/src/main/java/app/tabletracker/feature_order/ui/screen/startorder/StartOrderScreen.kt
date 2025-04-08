package app.tabletracker.feature_order.ui.screen.startorder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import app.tabletracker.R
import app.tabletracker.core.ui.component.TextBoxComponent
import app.tabletracker.feature_companion.model.ServerAction
import app.tabletracker.feature_companion.server.ACTION_REQUEST_SERVER_ADDRESS
import app.tabletracker.feature_companion.server.ACTION_SERVER_ADDRESS_AVAILABLE
import app.tabletracker.feature_companion.server.EXTRA_SERVER_ADDRESS
import app.tabletracker.feature_companion.server.SocketServerService
import app.tabletracker.feature_order.data.entity.OrderStatus
import app.tabletracker.feature_order.data.entity.OrderType
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems
import app.tabletracker.feature_order.ui.state.OrderUiEvent
import app.tabletracker.feature_order.ui.state.OrderViewModel
import app.tabletracker.theme.MaterialColor
import qrgenerator.qrkitpainter.rememberQrKitPainter

@Composable
fun StartOrderScreen(
    orderViewModel: OrderViewModel,
    modifier: Modifier = Modifier,
    onConnectWithCompanion: () -> Unit,
    onCreateNewOrder: () -> Unit,
) {
    val context = LocalContext.current

    BackHandler(true) {}
    val orderUiState by orderViewModel.uiState.collectAsState()

    var qrCodeDialogVisible by remember { mutableStateOf(false) }


    var serverAddress by remember { mutableStateOf("") }

    val receiver = remember {
        object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                val address = intent?.getStringExtra(EXTRA_SERVER_ADDRESS)
                address?.let {
                    serverAddress = it
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        val requestIntent = Intent(ACTION_REQUEST_SERVER_ADDRESS)
        context.sendBroadcast(requestIntent)
    }

    DisposableEffect(Unit) {
        val filter = IntentFilter(ACTION_SERVER_ADDRESS_AVAILABLE)
        ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        Intent(ACTION_SERVER_ADDRESS_AVAILABLE).also {
            it.setPackage(context.packageName)
            context.sendBroadcast(it)
        }

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BrandingSection()
        Spacer(modifier = Modifier.height(16.dp))

        val totalTransaction = calculateTotalTransaction(orderUiState.todayOrders)
        OverViewSection(
            totalTransaction = totalTransaction
        )

        Spacer(modifier = Modifier.height(32.dp))
        Row(
            horizontalArrangement = Arrangement.Center,

            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextBoxComponent(
                text = OrderType.DineIn.label,
                modifier = Modifier.padding(4.dp),
                textModifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                textStyle = MaterialTheme.typography.headlineMedium
            ) {
                orderViewModel.onEvent(OrderUiEvent.CreateNewOrder(OrderType.DineIn))
                onCreateNewOrder()
            }
            TextBoxComponent(
                text = OrderType.TakeOut.label,
                modifier = Modifier.padding(4.dp),
                textModifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                textStyle = MaterialTheme.typography.headlineMedium
            ) {
                orderViewModel.onEvent(OrderUiEvent.CreateNewOrder(OrderType.TakeOut))
                onCreateNewOrder()
            }
            TextBoxComponent(
                text = OrderType.Delivery.label,
                modifier = Modifier.padding(4.dp),
                textModifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                textStyle = MaterialTheme.typography.headlineMedium
            ) {
                orderViewModel.onEvent(OrderUiEvent.CreateNewOrder(OrderType.Delivery))
                onCreateNewOrder()
            }
        }
        Spacer(
            modifier = Modifier
                .weight(1f)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.End
        ) {
            if (serverAddress.isNotEmpty()) {
                IconButton(
                    onClick = {
                        qrCodeDialogVisible = true
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_qr_code_24),
                        contentDescription = "Show QR Code"
                    )
                }
            }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (serverAddress.isNotEmpty())
                        MaterialColor.Red.color
                    else MaterialColor.Blue.color
                ),
                onClick = {
                    if (serverAddress.isNotEmpty()) {
                        Intent(context.applicationContext, SocketServerService::class.java).also {
                            it.action = ServerAction.Stop.toString()
                            context.applicationContext.startService(it)
                        }
                        serverAddress = ""
                        qrCodeDialogVisible = false
                    } else {
                        Intent(context.applicationContext, SocketServerService::class.java).also {
                            it.action = ServerAction.Start.toString()
                            context.applicationContext.startService(it)
                        }
                        qrCodeDialogVisible = true
                    }
                }
            ) {
                Text("Connect a Companion Device")
            }
        }
    }
    if (qrCodeDialogVisible) {
        Dialog(
            onDismissRequest = { qrCodeDialogVisible = false },
        ) {
            val painter = rememberQrKitPainter(data = serverAddress)
            Surface(
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Scan from a Companion Device"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(serverAddress)
                }
            }
        }
    }

}

fun calculateTotalTransaction(orders: List<OrderWithOrderItems>): Float {
    val completedOrders = orders.filter {
        it.order.orderStatus == OrderStatus.Completed
    }
    var total = 0f
    completedOrders.forEach {
        total += it.order.discount?.value?.toFloatOrNull()
            ?.let { it1 -> it.order.totalPrice - (it.order.totalPrice * it1 / 100) }
            ?: 0f
    }
    return total
}