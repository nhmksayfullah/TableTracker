package app.tabletracker.feature_order.ui.screen.startorder

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.tabletracker.core.ui.component.TextBoxComponent
import app.tabletracker.feature_order.data.entity.OrderStatus
import app.tabletracker.feature_order.data.entity.OrderType
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems
import app.tabletracker.feature_order.ui.state.OrderUiEvent
import app.tabletracker.feature_order.ui.state.OrderViewModel

@Composable
fun StartOrderScreen(
    orderViewModel: OrderViewModel,
    modifier: Modifier = Modifier,
    onCreateNewOrder: () -> Unit,
) {
    BackHandler(true) {}
    val orderUiState by orderViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
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
                .fillMaxSize()
                .then(modifier)
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