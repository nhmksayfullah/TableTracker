package app.tabletracker.feature_order.v3.section


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.tabletracker.feature_order.data.entity.Discount
import app.tabletracker.feature_order.data.entity.Order


@Composable
fun CompleteOrderLeftSectionHeader3(
    order: Order,
    modifier: Modifier = Modifier,
    onPlaceOrder: () -> Unit = {},
    onCancelOrder: () -> Unit,
    onOrderChange: (Order) -> Unit
) {
    var addDiscountDialogState by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).then(modifier)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Sub Total:")
            Spacer(modifier = Modifier.weight(1f))
            Text("£%.2f".format(order.totalPrice))
        }
        if(order.discount != null) {
            val discount = order.discount.value.let {
                try {
                    it.toFloat() * order.totalPrice / 100
                } catch (e: Exception) {
                    e.printStackTrace()
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
        Row {
            Button(
                onClick = onPlaceOrder,
                modifier = Modifier.weight(1f)
            ) {
                Text("Place Order")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onCancelOrder,
                colors = ButtonDefaults.buttonColors().copy(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel Order"
                )
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
                Column {
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
                    Row {
                        InputChip(
                            onClick = {
                                discount = "5%"
                                onOrderChange(order.copy(discount = Discount(title = "", value = "5")))
                                addDiscountDialogState = false
                            },
                            label = {
                                Text("5%")
                            },
                            selected = false
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        InputChip(
                            onClick = {
                                discount = "clear"
                                onOrderChange(order.copy(discount = null))
                                addDiscountDialogState = false
                            },
                            label = {
                                Text("clear")
                            },
                            selected = false
                        )
                    }
                }
            },
            dismissButton = {
                Button(onClick = { addDiscountDialogState = false }) {
                    Text(text = "Dismiss")
                }
            }
        )
    }
}
