package app.tabletracker.feature_order.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.tabletracker.core.ui.SplitScreen
import app.tabletracker.core.ui.TabbedScreen
import app.tabletracker.feature_order.data.entity.OrderStatus
import app.tabletracker.feature_order.ui.OrderUiEvent
import app.tabletracker.feature_order.ui.OrderViewModel
import app.tabletracker.feature_order.ui.component.ShowOrderColumnNamesOnRightSection
import app.tabletracker.feature_order.ui.section.ShowOrderLeftSection
import app.tabletracker.feature_order.ui.section.ShowOrderListRightSection
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RunningOrderScreen(
    orderViewModel: OrderViewModel,
    onCustomizeOrderClick: (String) -> Unit
) {

    val orderUiState by orderViewModel.uiState.collectAsState()
    var selectedOrderId by rememberSaveable {
        mutableIntStateOf(-1)
    }

    val scope = rememberCoroutineScope()

    SplitScreen(
        leftContent = {
            if (selectedOrderId != -1) {
                if (orderUiState.currentOrder != null) {
                    ShowOrderLeftSection(
                        orderUiState = orderUiState,
                        order = orderUiState.currentOrder!!,
                        readOnly = true,
                        onItemChange = {},
                        onItemRemoveClick = {},
                        onPlaceOrderClick = {
                            if (orderUiState.currentOrder?.order?.orderStatus != OrderStatus.Completed) {
                                orderUiState.currentOrder?.order?.id?.let {
                                    onCustomizeOrderClick(it)
//                                selectedOrderId = -1
                                }
                            } else {
                                orderUiState.currentOrder?.order?.id?.let {
                                    onCustomizeOrderClick(it)
//                                selectedOrderId = -1
                                }
                            }

                        },
                        onCancelOrderClick = {},
                        onOrderUiEvent = {}
                    )
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Click an order to view or modify")
                }
            }
        },
        rightContent = {
            val orderStatusList = listOf(OrderStatus.Running.name, OrderStatus.Completed.name, OrderStatus.Cancelled.name)
            TabbedScreen(
                titles = orderStatusList,
                onClick = {selectedOrderId = -1}
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Column {
                    ShowOrderColumnNamesOnRightSection()
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(modifier = Modifier
                        .height(1.dp)
                        .padding(end = 16.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    when(OrderStatus.valueOf(orderStatusList[it])) {
                        OrderStatus.Running -> {
                            ShowOrderListRightSection(orders = orderUiState.runningOrders) {
                                selectedOrderId = it
                                orderViewModel.onEvent(OrderUiEvent.UpdateCurrentOrderWithOrderItems(orderUiState.runningOrders[it].order.id))
                            }
                        }
                        OrderStatus.Completed -> {
                            ShowOrderListRightSection(orders = orderUiState.completedOrders) {
                                selectedOrderId = it
                                orderViewModel.onEvent(OrderUiEvent.UpdateCurrentOrderWithOrderItems(orderUiState.completedOrders[it].order.id))
                            }
                        }
                        OrderStatus.Cancelled -> {
                            ShowOrderListRightSection(orders = orderUiState.cancelledOrders) {
                                selectedOrderId = it
                                orderViewModel.onEvent(OrderUiEvent.UpdateCurrentOrderWithOrderItems(orderUiState.cancelledOrders[it].order.id))
                            }
                        }

                        else -> {

                        }
                    }
                }

            }
        }
    )

}

