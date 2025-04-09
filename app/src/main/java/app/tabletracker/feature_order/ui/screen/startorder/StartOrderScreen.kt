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
import app.tabletracker.feature_companion.server.ACTION_CLIENT_CONNECTED
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


    BackHandler(true) {}
    val orderUiState by orderViewModel.uiState.collectAsState()

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
            CompanionSection()
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