package app.tabletracker.feature_order.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import app.tabletracker.util.TableTrackerDefault
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StartOrderScreen(
    modifier: Modifier = Modifier,
    orderViewModel: OrderViewModel,
    onClick: (String) -> Unit
) {
    BackHandler(true) {}
    val orderUiState by orderViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
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
            onClick(TableTrackerDefault.noOrderId)
        }
        TextBoxComponent(
            text = OrderType.TakeOut.label,
            modifier = Modifier.padding(8.dp),
            textModifier = Modifier.padding(36.dp),
            textStyle = MaterialTheme.typography.displaySmall
        ) {
            orderViewModel.onEvent(OrderUiEvent.CreateNewOrder(OrderType.TakeOut))
            onClick(TableTrackerDefault.noOrderId)
        }
        TextBoxComponent(
            text = OrderType.Delivery.label,
            modifier = Modifier.padding(8.dp),
            textModifier = Modifier.padding(36.dp),
            textStyle = MaterialTheme.typography.displaySmall
        ) {
            orderViewModel.onEvent(OrderUiEvent.CreateNewOrder(OrderType.Delivery))
            onClick(TableTrackerDefault.noOrderId)
        }
    }
}