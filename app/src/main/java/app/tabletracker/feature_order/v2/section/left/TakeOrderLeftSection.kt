package app.tabletracker.feature_order.v2.section.left

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import app.tabletracker.feature_order.data.entity.OrderStatus
import app.tabletracker.feature_order.data.entity.OrderType
import app.tabletracker.feature_order.v2.component.VerticalCarousel
import app.tabletracker.feature_order.v2.component.VerticalCarouselItem
import app.tabletracker.feature_order.v2.component.rememberVerticalCarouselState
import app.tabletracker.feature_order.v2.state.OrderUiEvent2
import app.tabletracker.feature_order.v2.state.OrderUiState2
import app.tabletracker.feature_printing.domain.PrinterManager
import app.tabletracker.feature_receipt.domain.ReceiptGenerator

@Composable
fun TakeOrderLeftSection(
    orderUiState: OrderUiState2,
    modifier: Modifier = Modifier,
    onOrderUiEvent: (OrderUiEvent2) -> Unit,
    onOrderDismiss: () -> Unit
) {
    val state = rememberVerticalCarouselState(3)
    val context = LocalContext.current

    LaunchedEffect(orderUiState.currentOrder?.order?.orderType) {
        orderUiState.currentOrder?.order?.let {
            if (it.orderType == OrderType.DineIn) {
                state.toggleExpand(1)
            } else {
                state.toggleExpand(0)
            }
        }
    }

    VerticalCarousel(state) {
        // Customer Section
        VerticalCarouselItem(
            isExpanded = state.isExpanded(0),
            onToggleExpand = { state.toggleExpand(0) },
            header = {
                CustomerLeftSectionHeader()
            },
            content = {
                CustomerLeftSectionContent(
                    customer = orderUiState.currentOrder?.order?.customer,
                    onCustomerChange = {
                        onOrderUiEvent(OrderUiEvent2.UpdateCustomer(it))
                    }
                )
            }
        )
        // Order Details Section
        VerticalCarouselItem(
            isExpanded = state.isExpanded(1),
            onToggleExpand = { state.toggleExpand(1) },
            header = {
                EditOrderLeftSectionHeader(
                    orderNumber = orderUiState.currentOrder?.order?.orderNumber.toString(),
                )
            },
            content = {
                orderUiState.currentOrder?.let {
                    EditOrderLeftSectionContent(
                        runningOrder = it,
                        onItemRemoveClick = {
                            onOrderUiEvent(OrderUiEvent2.RemoveItemFromOrder(it))
                        },
                        onItemChange = {
                            onOrderUiEvent(OrderUiEvent2.UpdateOrderItem(it))
                        }
                    )
                }
            }
        )

        // Complete Order Section
        VerticalCarouselItem(
            isExpanded = state.isExpanded(2),
            onToggleExpand = { state.toggleExpand(2) },
            header = {
                orderUiState.currentOrder?.order?.let {
                    CompleteOrderLeftSectionHeader(
                        order = it,
                        onCancelOrder = {
                            onOrderUiEvent(OrderUiEvent2.UpdateCurrentOrder(it.copy(orderStatus = OrderStatus.Cancelled)))
                            onOrderDismiss()
                        }
                    )
                }
            },
            content = {
                orderUiState.currentOrder?.order?.let {
                    CompleteOrderLeftSectionContent(
                        order = it,
                        totalTable = orderUiState.restaurantExtra?.totalTable ?: 0,
                        runningOrders = orderUiState.runningOrders,
                        onOrderChange = {
                            onOrderUiEvent(OrderUiEvent2.UpdateCurrentOrder(it))
                        },
                        onCompleteOrder = {
                            val printerManager = PrinterManager(context as Activity)
                            val receiptGenerator =
                                ReceiptGenerator(orderUiState.restaurantInfo, orderUiState.currentOrder)
                            printerManager.print(receiptGenerator.generateReceipt())
                            onOrderUiEvent(OrderUiEvent2.UpdateCurrentOrder(it.copy(orderStatus = OrderStatus.Completed)))
                            onOrderDismiss()
                        },
                        onSaveOrder = {
                            val printerManager = PrinterManager(context as Activity)
                            val receiptGenerator =
                                ReceiptGenerator(orderUiState.restaurantInfo, orderUiState.currentOrder)
                            printerManager.print(receiptGenerator.generateKitchenCopy())
                            onOrderUiEvent(OrderUiEvent2.UpdateCurrentOrder(it.copy(orderStatus = OrderStatus.Running)))
                            onOrderDismiss()
                        }
                    )
                }
            }
        )
    }

}