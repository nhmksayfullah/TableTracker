package app.tabletracker.feature_order.v2.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.tabletracker.core.ui.component.TextBoxComponent
import app.tabletracker.feature_order.data.entity.OrderType
import app.tabletracker.feature_order.ui.OrderUiEvent
import app.tabletracker.feature_order.ui.OrderViewModel
import app.tabletracker.feature_order.v2.state.OrderUiEvent2
import app.tabletracker.feature_order.v2.state.OrderViewModel2

@Composable
fun StartOrderScreen2(
    orderViewModel: OrderViewModel,
    modifier: Modifier = Modifier,
    onCreateNewOrder: () -> Unit,
) {
    BackHandler(true) {}
    val orderUiState by orderViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .then(modifier),
        contentAlignment = Alignment.TopEnd
    ) {
        Text("Total Orders: ${orderUiState.todayOrders.lastOrNull()?.order?.orderNumber ?: 0}")
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        TextBoxComponent(
            text = OrderType.DineIn.label,
            modifier = Modifier.padding(8.dp),
            textModifier = Modifier.padding(36.dp),
            textStyle = MaterialTheme.typography.displaySmall
        ) {
            orderViewModel.onEvent(OrderUiEvent.CreateNewOrder(OrderType.DineIn))
            onCreateNewOrder()
        }
        TextBoxComponent(
            text = OrderType.TakeOut.label,
            modifier = Modifier.padding(8.dp),
            textModifier = Modifier.padding(36.dp),
            textStyle = MaterialTheme.typography.displaySmall
        ) {
            orderViewModel.onEvent(OrderUiEvent.CreateNewOrder(OrderType.TakeOut))
            onCreateNewOrder()
        }
        TextBoxComponent(
            text = OrderType.Delivery.label,
            modifier = Modifier.padding(8.dp),
            textModifier = Modifier.padding(36.dp),
            textStyle = MaterialTheme.typography.displaySmall
        ) {
            orderViewModel.onEvent(OrderUiEvent.CreateNewOrder(OrderType.Delivery))
            onCreateNewOrder()
        }
    }
}