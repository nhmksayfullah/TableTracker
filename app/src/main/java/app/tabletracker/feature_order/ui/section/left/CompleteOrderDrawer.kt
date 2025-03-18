package app.tabletracker.feature_order.ui.section.left

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import app.tabletracker.auth.data.model.Restaurant
import app.tabletracker.core.ui.component.DialogKeyboardType
import app.tabletracker.core.ui.component.DisabledTextField
import app.tabletracker.core.ui.component.KeyboardDialog
import app.tabletracker.feature_order.data.entity.OrderItemStatus
import app.tabletracker.feature_order.data.entity.OrderStatus
import app.tabletracker.feature_order.data.entity.OrderType
import app.tabletracker.feature_order.data.entity.PaymentMethod
import app.tabletracker.feature_order.ui.state.OrderUiEvent
import app.tabletracker.feature_order.ui.state.OrderUiState
import app.tabletracker.feature_printing.domain.PrinterManager
import app.tabletracker.feature_receipt.domain.ReceiptGenerator
import app.tabletracker.util.TableTrackerDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompleteOrderDrawer(
    orderUiState: OrderUiState,
    orderUiEvent: (OrderUiEvent) -> Unit,
    printKitchenCopy: Boolean = true,
    onOrderDismiss: (OrderUiEvent?) -> Unit
) {
    val orderTypes = TableTrackerDefault.availableOrderTypes
    val paymentMethods = TableTrackerDefault.availablePaymentMethods
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Scaffold(
            bottomBar = {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (orderUiState.currentOrder != null) {
                                if (orderUiState.currentOrder.orderItems.find {
                                        it.orderItemStatus == OrderItemStatus.Added &&
                                                it.menuItem.isKitchenCategory
                                    } != null) {
                                    val printerManager = PrinterManager(context as Activity)
                                    val receiptGenerator =
                                        ReceiptGenerator(
                                            orderUiState.restaurantInfo,
                                            orderUiState.currentOrder
                                        )
                                    printerManager.print(receiptGenerator.generateKitchenCopy())
                                }

                                orderUiEvent(
                                    OrderUiEvent.UpdateCurrentOrder(
                                        orderUiState.currentOrder.order.copy(
                                            orderStatus = OrderStatus.Running
                                        )
                                    )
                                )
                                onOrderDismiss(null)
                            }
                        }
                    ) {
                        Text(text = "Save Order")
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (orderUiState.currentOrder != null) {
                                val printerManager = PrinterManager(context as Activity)
                                val receiptGenerator =
                                    ReceiptGenerator(
                                        orderUiState.restaurantInfo,
                                        orderUiState.currentOrder
                                    )
                                if (orderUiState.currentOrder.orderItems.find {
                                        it.orderItemStatus == OrderItemStatus.Added &&
                                                it.menuItem.isKitchenCategory
                                    } != null) {
                                    printerManager.print(receiptGenerator.generateKitchenCopy())
                                }
                                printerManager.print(receiptGenerator.generateReceipt())

                                orderUiEvent(
                                    OrderUiEvent.UpdateCurrentOrder(
                                        orderUiState.currentOrder.order.copy(
                                            orderStatus = OrderStatus.Completed
                                        )
                                    )
                                )
                                onOrderDismiss(null)
                            }
                        }
                    ) {
                        Text(text = "Complete Order")
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(text = "Order Type")
                Spacer(modifier = Modifier.height(8.dp))
                SingleChoiceSegmentedButtonRow {
                    orderTypes.forEachIndexed { index, orderType ->
                        SegmentedButton(
                            selected = orderType == orderUiState.currentOrder?.order?.orderType,
                            onClick = {
                                if (orderUiState.currentOrder != null) {
                                    orderUiEvent(
                                        OrderUiEvent.UpdateCurrentOrder(
                                            orderUiState.currentOrder.order.copy(
                                                orderType = orderType
                                            )
                                        )
                                    )
                                }
                            },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = orderTypes.size
                            )
                        ) {
                            Text(text = orderType.label)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Select Payment Method")
                Spacer(modifier = Modifier.height(8.dp))
                SingleChoiceSegmentedButtonRow {
                    paymentMethods.forEachIndexed { index, paymentMethod ->

                        SegmentedButton(
                            selected = paymentMethod == orderUiState.currentOrder?.order?.paymentMethod,
                            onClick = {
                                if (orderUiState.currentOrder != null) {
                                    if (paymentMethod == orderUiState.currentOrder.order.paymentMethod) {
                                        orderUiEvent(
                                            OrderUiEvent.UpdateCurrentOrder(
                                                orderUiState.currentOrder.order.copy(
                                                    paymentMethod = PaymentMethod.None
                                                )
                                            )
                                        )
                                    } else {
                                        orderUiEvent(
                                            OrderUiEvent.UpdateCurrentOrder(
                                                orderUiState.currentOrder.order.copy(
                                                    paymentMethod = paymentMethod
                                                )
                                            )
                                        )
                                    }

                                }
                            },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = orderTypes.size
                            )
                        ) {
                            Text(text = paymentMethod.name)
                        }
                    }
                }

                if (orderUiState.currentOrder?.order?.orderType == OrderType.DineIn) {
                    Spacer(modifier = Modifier.height(16.dp))
                    val tableNumbers = mutableListOf<Int>()
                    if (orderUiState.restaurantExtra?.totalTable != 0) {
                        for (i in 1..orderUiState.restaurantExtra!!.totalTable) {
                            val availableTables =
                                orderUiState.runningOrders.map { it.order.tableNumber }
                            if (i !in availableTables) {
                                tableNumbers.add(i)
                            }

                        }
                    }

                    var expendedState by rememberSaveable {
                        mutableStateOf(false)
                    }
                    var selectedTable by rememberSaveable {
                        mutableStateOf("Select a Table")
                    }

                    ExposedDropdownMenuBox(
                        expanded = expendedState,
                        onExpandedChange = {
                            expendedState = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val fieldValue: String =
                            if (orderUiState.currentOrder.order.tableNumber != null) {
                                "Table ${orderUiState.currentOrder.order.tableNumber}"
                            } else selectedTable
                        TextField(
                            value = fieldValue,
                            onValueChange = {
                                selectedTable = it
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            readOnly = true,
                            singleLine = true,
                            label = { Text(text = "Table Number") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expendedState)
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = expendedState,
                            onDismissRequest = { expendedState = false }) {
                            tableNumbers.forEach { tableNumber ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "Table $tableNumber",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    },
                                    onClick = {
                                        selectedTable = tableNumber.toString()
                                        expendedState = false
                                        orderUiEvent(
                                            OrderUiEvent.UpdateCurrentOrder(
                                                orderUiState.currentOrder.order.copy(
                                                    tableNumber = tableNumber
                                                )
                                            )
                                        )
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    var drawerState by rememberSaveable {
                        mutableStateOf(false)
                    }
                    DisabledTextField(
                        value = "${orderUiState.currentOrder.order.totalPerson ?: 0}",
                        label = "Total Person",
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        drawerState = true
                    }

                    if (drawerState) {
                        KeyboardDialog(
                            onDismissRequest = { /*TODO*/ },
                            value = (orderUiState.currentOrder.order.totalPerson ?: "").toString(),
                            label = "Total Person",
                            keyboardType = DialogKeyboardType.Numeric,
                            dialogState = drawerState
                        ) {
                            orderUiEvent(
                                OrderUiEvent.UpdateCurrentOrder(
                                    orderUiState.currentOrder.order.copy(
                                        totalPerson = it.toIntOrNull()
                                    )
                                )
                            )
                            drawerState = false
                        }
                    }


                }

            }
        }
    }


}

val dummyRestaurant = Restaurant(
    name = "Madras Spice Restaurant",
    address = "180 Northenden Rd, Sale M33 2SR",
    contactNumber = "07123456789",
    licence = "",
    website = "www.madras-spice.uk",
    vatNumber = "303043464"
)