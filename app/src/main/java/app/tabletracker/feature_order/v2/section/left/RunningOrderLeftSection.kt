package app.tabletracker.feature_order.v2.section.left

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import app.tabletracker.feature_order.ui.OrderUiState
import app.tabletracker.feature_order.v2.component.VerticalCarousel
import app.tabletracker.feature_order.v2.component.VerticalCarouselItem
import app.tabletracker.feature_order.v2.component.rememberVerticalCarouselState
import app.tabletracker.feature_order.v2.state.OrderUiState2
import app.tabletracker.feature_printing.domain.PrinterManager
import app.tabletracker.feature_receipt.domain.ReceiptGenerator

@Composable
fun RunningOrderLeftSection(
    orderUiState: OrderUiState,
    modifier: Modifier = Modifier,
    onCustomizeCurrentOrder: () -> Unit,
) {
    val state = rememberVerticalCarouselState(3)
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        state.toggleExpand(2)
    }

    VerticalCarousel(state) {
        VerticalCarouselItem(
            isExpanded = state.isExpanded(0),
            onToggleExpand = { state.toggleExpand(0) },
            header = {
                CustomerLeftSectionHeader()
            },
            content = {
                CustomerLeftSectionContent(
                    customer = orderUiState.currentOrder?.order?.customer,
                    onCustomerChange = {}
                )
            }
        )

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

                        },
                        onItemChange = {

                        }
                    )
                }
            }
        )

        VerticalCarouselItem(
            isExpanded = state.isExpanded(2),
            onToggleExpand = { state.toggleExpand(2) },
            header = {
                CustomizeOrderLeftSectionHeader()
            },
            content = {
                orderUiState.currentOrder?.order?.let {
                    CustomizeOrderLeftSectionContent(
                        order = it,
                        onCustomizeOrder = onCustomizeCurrentOrder,
                        onPrintReceipt = {
                            val printerManager = PrinterManager(context as Activity)
                            val receiptGenerator =
                                ReceiptGenerator(orderUiState.restaurantInfo, orderUiState.currentOrder)
                            printerManager.print(receiptGenerator.generateReceipt())
                        },
                        onPrintKitchenCopy = {
                            val printerManager = PrinterManager(context as Activity)
                            val receiptGenerator =
                                ReceiptGenerator(orderUiState.restaurantInfo, orderUiState.currentOrder)
                            printerManager.print(receiptGenerator.generateKitchenCopy(printFullKitchenCopy = true))
                        }
                    )
                }
            }
        )
    }
}