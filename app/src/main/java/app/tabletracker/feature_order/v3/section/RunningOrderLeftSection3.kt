package app.tabletracker.feature_order.v3.section

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import app.tabletracker.core.ui.TabbedScreen
import app.tabletracker.feature_order.ui.OrderUiState
import app.tabletracker.feature_order.v2.section.left.CustomizeOrderLeftSectionContent
import app.tabletracker.feature_printing.domain.PrinterManager
import app.tabletracker.feature_receipt.domain.ReceiptGenerator

@Composable
fun RunningOrderLeftSection3(
    orderUiState: OrderUiState,
    modifier: Modifier = Modifier,
    onCustomizeCurrentOrder: () -> Unit,
) {
    val context = LocalContext.current
    Scaffold(
        bottomBar = {
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
    ) {
        Column(
            modifier = modifier
                .padding(it)
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
                                },
                                onItemChange = {
                                }
                            )
                        }
                    }
                    1 -> {
                        CustomerLeftSectionContent3(
                            customer = orderUiState.currentOrder?.order?.customer,
                            onCustomerChange = {
                            }
                        )
                    }
                }
            }
        }
    }
}