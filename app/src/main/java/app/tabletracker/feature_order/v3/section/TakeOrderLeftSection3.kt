package app.tabletracker.feature_order.v3.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.tabletracker.core.ui.TabbedScreen
import app.tabletracker.feature_order.data.entity.OrderStatus
import app.tabletracker.feature_order.ui.OrderUiEvent
import app.tabletracker.feature_order.ui.OrderUiState

@Composable
fun TakeOrderLeftSection3(
    orderUiState: OrderUiState,
    modifier: Modifier = Modifier,
    onPlaceOrder: () -> Unit,
    onOrderUiEvent: (OrderUiEvent) -> Unit,
    onCancelOrder: (OrderUiEvent) -> Unit
) {
    Scaffold(
        bottomBar = {
            orderUiState.currentOrder?.let {
                CompleteOrderLeftSectionHeader3(
                    order = it.order,
                    onPlaceOrder = onPlaceOrder,
                    onCancelOrder = {
                        onCancelOrder(OrderUiEvent.UpdateCurrentOrder(
                            orderUiState.currentOrder.order.copy(
                                orderStatus = OrderStatus.Cancelled
                            )
                        ))
                    },
                    onOrderChange = {
                        onOrderUiEvent(OrderUiEvent.UpdateCurrentOrder(it))
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TabbedScreen(
                titles = listOf("Order Details", "Customer Details")
            ) {
                when (it) {
                    0 -> {
                        orderUiState.currentOrder?.let {
                            EditOrderLeftSectionContent3(
                                runningOrder = it,
                                onItemRemoveClick = {
                                    onOrderUiEvent(OrderUiEvent.RemoveItemFromOrder(it))
                                },
                                onItemChange = {
                                    onOrderUiEvent(OrderUiEvent.UpdateOrderItem(it))
                                }
                            )
                        }
                    }
                    1 -> {
                        CustomerLeftSectionContent3(
                            customer = orderUiState.currentOrder?.order?.customer,
                            onCustomerChange = {
                                onOrderUiEvent(OrderUiEvent.UpdateCustomer(it))
                            }
                        )
                    }
                }
            }
        }
    }
}