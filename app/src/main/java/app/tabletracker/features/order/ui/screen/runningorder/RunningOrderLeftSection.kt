package app.tabletracker.features.order.ui.screen.runningorder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import app.tabletracker.core.ui.TabbedScreen
import app.tabletracker.features.order.ui.screen.takeorder.CustomerDetailsFormLeftSection
import app.tabletracker.features.order.ui.screen.takeorder.OrderItemsComponentLeftSection
import app.tabletracker.features.order.ui.state.OrderUiState
import app.tabletracker.features.printing.domain.PrinterManager
import app.tabletracker.features.receipt.domain.ReceiptGenerator

@Composable
fun RunningOrderLeftSection(
    orderUiState: OrderUiState,
    modifier: Modifier = Modifier,
    onCustomizeCurrentOrder: () -> Unit,
) {
    val context = LocalContext.current
    Scaffold(
        bottomBar = {
            orderUiState.currentOrder?.order?.let {
                RunningOrderSummaryLeftSection(
                    order = it,
                    onCustomizeOrder = onCustomizeCurrentOrder,
                    onPrintReceipt = {
                        val printerManager = PrinterManager(context)
                        val receiptGenerator =
                            ReceiptGenerator(orderUiState.restaurantInfo, orderUiState.currentOrder)
                        printerManager.print(receiptGenerator.generateReceipt())
                    },
                    onPrintKitchenCopy = {
                        val printerManager = PrinterManager(context)
                        val receiptGenerator =
                            ReceiptGenerator(orderUiState.restaurantInfo, orderUiState.currentOrder)
                        printerManager.print(
                            receiptGenerator.generateKitchenCopy(
                                printFullKitchenCopy = true
                            )
                        )
                    }
                )
            }
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {
            TabbedScreen(
                titles = listOf("Items", "Customer", "Details"),
            ) {
                when (it) {
                    0 -> {
                        orderUiState.currentOrder?.let {
                            OrderItemsComponentLeftSection(
                                runningOrder = it,
                                onItemRemoveClick = {
                                },
                                onItemChange = {
                                }
                            )
                        }
                    }

                    1 -> {
                        CustomerDetailsFormLeftSection(
                            customer = orderUiState.currentOrder?.order?.customer,
                            onCustomerChange = {
                            }
                        )
                    }

                    2 -> {

                    }
                }
            }
        }
    }
}