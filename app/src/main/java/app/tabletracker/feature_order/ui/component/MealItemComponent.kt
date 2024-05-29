package app.tabletracker.feature_order.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import app.tabletracker.R
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_order.data.entity.OrderItem
import app.tabletracker.feature_order.data.entity.OrderType
import app.tabletracker.feature_order.data.entity.toOrderItem
import app.tabletracker.feature_order.ui.section.AddEditMealMenuDialogSection

@Composable
fun MealItemComponent(
    orderId: String,
    orderItem: OrderItem,
    orderType: OrderType,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    onItemRemoveClick: () -> Unit,
    onItemChange: (OrderItem) -> Unit
) {
    var orderItemsVisibility by rememberSaveable {
        mutableStateOf(false)
    }
    var dialogState by rememberSaveable {
        mutableStateOf(false)
    }

//    var selectedMealItemId by rememberSaveable {
//        mutableStateOf(orderItem.menuItem.id)
//    }

    var dropDownState by rememberSaveable {
        mutableStateOf(false)
    }
    var addedNote by rememberSaveable {
        mutableStateOf("")
    }

    Column {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (!readOnly) {
                        onItemRemoveClick()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Remove item from the order"
                )
            }

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = orderItem.menuItem.name)
                // Quantity:: this section plays with the quantity of an item.
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {

                    // Decrease:: remove one quantity on every click
                    IconButton(onClick = {
                        if (!readOnly) {
                            if (orderItem.quantity == 1) {
                                onItemRemoveClick()
                                addedNote = ""
                                dropDownState = false
                            } else {
                                onItemChange(orderItem.copy(quantity = orderItem.quantity - 1))
                            }
                        }
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.remove_circle_outlined),
                            contentDescription = "Decrease Quantity"
                        )

                    }

                    // Quantity:: shows the quantity
                    Text(text = orderItem.quantity.toString())

                    // Increase:: add one quantity on every click
                    IconButton(onClick = {
                        if(!readOnly) {
                            onItemChange(orderItem.copy(quantity = orderItem.quantity + 1))
                        }
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.add_circle_outline),
                            contentDescription = "Increase Quantity"
                        )
                    }
                }
            }

            // Price & DropDown:: this section shows the price and the dropdown button
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                val unitPrice = orderItem.menuItem.prices[orderType] ?: 0.0f

                // Price:: shows the price of the item.
                Text(text = "£%.2f".format(unitPrice.times(orderItem.quantity)))

                // DropDown:: click to expend the order item to customize the order.
                IconButton(
                    onClick = {
                        orderItemsVisibility = !orderItemsVisibility
                        dialogState = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Customize the item"
                    )
                }
            }
        }
        Column(
            modifier = Modifier.padding(start = 32.dp, bottom = 8.dp)
        ) {
            orderItem.menuItem.mealCourses.forEach {
                if (it.selectedItem != null) {
                    Row {
                        Text(
                            text = it.selectedItem.name,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        }
        if (dropDownState) {
            OrderItemCustomizeSection(
                addedNote = if (readOnly) orderItem.addedNote else addedNote,
                readOnly = readOnly
            ) {
                if (!readOnly) {
                    addedNote = it
                    onItemChange(orderItem.copy(addedNote = addedNote))
                }
            }
        }
        // It draws a horizontal line on the bottom of the item to separate it from the next item.
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onBackground)
        )

    }

    if (dialogState) {
        AddEditMealMenuDialogSection(
            menuItem = orderItem.menuItem,
            onMenuItemClicked = {
                onItemChange(orderItem.copy(menuItem = it))
            },
            onDialogDismiss = {
                dialogState = false
            }
        )
    }
}