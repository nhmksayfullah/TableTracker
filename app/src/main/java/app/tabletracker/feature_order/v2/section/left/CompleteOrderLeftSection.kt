package app.tabletracker.feature_order.v2.section.left

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.tabletracker.core.ui.component.DialogKeyboardType
import app.tabletracker.core.ui.component.DisabledTextField
import app.tabletracker.core.ui.component.KeyboardDialog
import app.tabletracker.feature_order.data.entity.Discount
import app.tabletracker.feature_order.data.entity.Order
import app.tabletracker.feature_order.data.entity.OrderType
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems
import app.tabletracker.util.TableTrackerDefault


@Composable
fun CompleteOrderLeftSectionHeader(
    order: Order,
    modifier: Modifier = Modifier,
    onCancelOrder: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Sub Total: £%.2f".format(order.totalPrice))
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = onCancelOrder,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel Order")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompleteOrderLeftSectionContent(
    order: Order,
    totalTable: Int = 0,
    runningOrders: List<OrderWithOrderItems> = emptyList(),
    modifier: Modifier = Modifier,
    onOrderChange: (Order) -> Unit,
    onCompleteOrder: () -> Unit,
    onSaveOrder: () -> Unit
) {
    var addDiscountDialogState by rememberSaveable {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        bottomBar = {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onSaveOrder
                ) {
                    Text(text = "Save Order")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCompleteOrder
                ) {
                    Text(text = "Complete Order")
                }
            }
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if(order.discount != null) {
                val discount = order.discount.value.let {
                    try {
                        it.toFloat() * order.totalPrice / 100
                    } catch (e: Exception) {
                        0.0f
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Discount:")
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        addDiscountDialogState = true
                    }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription =null)
                    }
                    Text("-£%.2f".format(discount))
                }
                Row {
                    Text(text = "Total:")
                    Spacer(modifier = Modifier.weight(1f))
                    Text("£%.2f".format(order.totalPrice - discount))
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(onClick = {
                        addDiscountDialogState = true
                    }) {
                        Text(text = "Add Discount")
                    }
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))

            Text(text = "Order Type")
            Spacer(modifier = Modifier.height(8.dp))
            SingleChoiceSegmentedButtonRow {
                TableTrackerDefault.availableOrderTypes.forEachIndexed { index, orderType ->
                    SegmentedButton(
                        selected = orderType == order.orderType,
                        onClick = {
                            onOrderChange(order.copy(orderType = orderType))
                        },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = TableTrackerDefault.availableOrderTypes.size
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
                TableTrackerDefault.availablePaymentMethods.forEachIndexed { index, paymentMethod ->
                    SegmentedButton(
                        selected = paymentMethod == order.paymentMethod,
                        onClick = {
                            onOrderChange(order.copy(paymentMethod = paymentMethod))
                        },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = TableTrackerDefault.availablePaymentMethods.size
                        )
                    ) {
                        Text(text = paymentMethod.name)
                    }
                }
            }
            if (order.orderType == OrderType.DineIn) {
                Spacer(modifier = Modifier.height(16.dp))
                val tableNumbers = mutableListOf<Int>()
                if (totalTable != 0) {
                    for (i in 1..totalTable) {
                        val availableTables =
                            runningOrders.map { it.order.tableNumber }
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
                        if (order.tableNumber != null) {
                            "Table ${order.tableNumber}"
                        } else "Table: $selectedTable"
                    OutlinedTextField(
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
                                    onOrderChange(order.copy(tableNumber = tableNumber))
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
                    value = "${order.totalPerson ?: 0}",
                    label = "Total Person",
                    modifier = Modifier.fillMaxWidth()
                ) {
                    drawerState = true
                }

                if (drawerState) {
                    KeyboardDialog(
                        onDismissRequest = { /*TODO*/ },
                        value = (order.totalPerson ?: "").toString(),
                        label = "Total Person",
                        keyboardType = DialogKeyboardType.Numeric,
                        dialogState = drawerState
                    ) {
                        onOrderChange(order.copy(totalPerson = it.toIntOrNull()))
                        drawerState = false
                    }
                }


            }
        }
    }
    if (addDiscountDialogState) {
        var discount by rememberSaveable {
            mutableStateOf(order.discount?.value ?: "")
        }
        AlertDialog(
            onDismissRequest = { addDiscountDialogState = false},
            confirmButton = {
                Button(onClick = {
                    if (discount.isNotEmpty()) {
                        onOrderChange(order.copy(discount = Discount(title = "", value = discount)))
                        addDiscountDialogState = false
                    }
                }) {
                    Text(text = "Add Discount")
                }
            },
            title = {

                TextField(
                    value = discount,
                    onValueChange = {
                        discount = it
                    },
                    label = {
                        Text(text = "Discount (%)")
                    },
                    placeholder = {
                        Text(text = "15")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            },
            dismissButton = {
                Button(onClick = { addDiscountDialogState = false }) {
                    Text(text = "Dismiss")
                }
            }
        )
    }
}