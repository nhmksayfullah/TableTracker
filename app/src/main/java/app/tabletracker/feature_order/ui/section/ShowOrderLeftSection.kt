package app.tabletracker.feature_order.ui.section

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.tabletracker.core.ui.TabbedScreen
import app.tabletracker.feature_customer.data.model.Customer
import app.tabletracker.feature_order.data.entity.OrderItem
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems
import app.tabletracker.feature_order.ui.OrderUiEvent
import app.tabletracker.feature_order.ui.component.OrderItemComponent


@Composable
fun ShowOrderLeftSection(
    order: OrderWithOrderItems,
    readOnly: Boolean = false,
    onItemRemoveClick: (OrderItem) -> Unit,
    onItemChange: (OrderItem) -> Unit,
    onPlaceOrderClick: () -> Unit,
    onCancelOrderClick: () -> Unit,
    onOrderUiEvent: (OrderUiEvent) -> Unit
) {

    var cancelOrderDialogState by rememberSaveable {
        mutableStateOf(false)
    }
    var addOrderToItemDialogState by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Row {
                            Text(text = "Sub Total:")
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = "£${order.order.totalPrice}")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row{
                    Button(
                        modifier = Modifier
                            .weight(.8f)
                            .padding(end = 2.dp),
                        onClick = {
                            if (readOnly) {
                                onPlaceOrderClick()
                            } else {
                                if (order.orderItems.isEmpty()) {
                                    addOrderToItemDialogState = true
                                } else {
                                    onPlaceOrderClick()
                                }
                            }
                        }
                    ) {
                        Text(text = if (readOnly) "Customize Order" else "Place Order")
                    }
                    if (!readOnly) {
                        Button(
                            modifier = Modifier
                                .weight(.2f)
                                .padding(start = 2.dp),
                            colors = ButtonDefaults.buttonColors().copy(containerColor = MaterialTheme.colorScheme.error),
                            onClick = {
                                cancelOrderDialogState = true
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "cancel order")
                        }
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(8.dp)
        ) {
            Text(
                text = "Order No: ${order.order.orderNumber}"
            )
            TabbedScreen(
                titles = listOf("Items", "Customer")
            ) {
                when(it) {
                    0 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            LazyColumn {
                                items(order.orderItems) {orderItem ->
                                    OrderItemComponent(
                                        orderItem = orderItem,
                                        readOnly = readOnly,
                                        orderType = order.order.orderType,
                                        onItemRemoveClick = {
                                            onItemRemoveClick(orderItem)
                                        },
                                        onItemChange = { updatedItem ->
                                            onItemChange(updatedItem)
                                        }
                                    )
                                }
                            }
                        }
                    }
                    1 -> {

                        AddCustomerLeftSection(
                            readOnly = readOnly,
                            customer = order.order.customer,
                            onCustomerChange = {
                                onOrderUiEvent(OrderUiEvent.UpdateCurrentOrder(order.order.copy(customer = it)))
                            }
                        )
                        Log.d("current order: customer", order.order.toString())
                    }
                }
            }
        }
    }

    if (cancelOrderDialogState) {
        AlertDialog(
            onDismissRequest = { cancelOrderDialogState = false },
            confirmButton = {
                TextButton(onClick = {
                    cancelOrderDialogState = false
                    onCancelOrderClick()
                }) {
                    Text(text = "Cancel Order")
                }
            },
            title = {
                Text(text = "Are you sure?")
            },
            dismissButton = {
                TextButton(onClick = { cancelOrderDialogState = false }) {
                    Text(text = "Dismiss")
                }
            }
        )
    }
    if (addOrderToItemDialogState) {
        AlertDialog(
            onDismissRequest = { addOrderToItemDialogState = false },
            confirmButton = {
                TextButton(onClick = {
                    addOrderToItemDialogState = false
                }) {
                    Text(text = "Add Item")
                }
            },
            title = {
                Text(text = "Please add item to order")
            }
        )
    }

}