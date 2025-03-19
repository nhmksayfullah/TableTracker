package app.tabletracker.feature_order.ui.screen.takeorder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.tabletracker.core.ui.TabbedScreen
import app.tabletracker.feature_order.data.entity.OrderStatus
import app.tabletracker.feature_order.ui.state.OrderUiEvent
import app.tabletracker.feature_order.ui.state.OrderUiState

@Composable
fun TakeOrderScreenLeftSection(
    orderUiState: OrderUiState,
    modifier: Modifier = Modifier,
    onPlaceOrder: () -> Unit,
    onOrderUiEvent: (OrderUiEvent) -> Unit,
    onCancelOrder: (OrderUiEvent) -> Unit
) {
    Scaffold(
        bottomBar = {
            orderUiState.currentOrder?.let {
                OrderSummaryLeftSection(
                    order = it.order,
                    onPlaceOrder = onPlaceOrder,
                    onCancelOrder = {
                        onCancelOrder(
                            OrderUiEvent.UpdateCurrentOrder(
                                orderUiState.currentOrder.order.copy(
                                    orderStatus = OrderStatus.Cancelled
                                )
                            )
                        )
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
            Text(
                text = orderUiState.currentOrder?.order?.orderNumber.toString()
            )
            TabbedScreen(
                titles = listOf("Order Details", "Customer Details")
            ) {
                when (it) {
                    0 -> {
                        orderUiState.currentOrder?.let {
                            OrderItemsComponentLeftSection(
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
                        CustomerDetailsFormLeftSection(
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