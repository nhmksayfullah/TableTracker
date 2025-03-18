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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.tabletracker.R
import app.tabletracker.core.ui.component.KeyboardDialog
import app.tabletracker.feature_menu.data.DummyData
import app.tabletracker.feature_order.data.entity.OrderItem
import app.tabletracker.feature_order.data.entity.OrderItemStatus
import app.tabletracker.feature_order.data.entity.OrderType
import app.tabletracker.theme.TableTrackerTheme


// this is the order item component
@Composable
fun OrderItemComponent(
    orderItem: OrderItem,
    orderType: OrderType,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    onItemRemoveClick: () -> Unit,
    onItemChange: (OrderItem) -> Unit
) {
    var keyboardState by rememberSaveable {
        mutableStateOf(false)
    }
    var addedNote by rememberSaveable {
        mutableStateOf(orderItem.addedNote)
    }

    // Add this LaunchedEffect to keep local state in sync with orderItem
    LaunchedEffect(orderItem.addedNote) {
        addedNote = orderItem.addedNote
    }

    Column(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .then(modifier),
    ) {

        // this section is the main visible section.
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Remove:: removes an item from the order list
            IconButton(
                onClick = {
                    if (!readOnly) {
                        onItemRemoveClick()
                        addedNote = ""
                        keyboardState = false
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
                // ItemName:: name of the item
                Row {
                    Text(
                        text = orderItem.menuItem.name
                    )
//                    Spacer(modifier = Modifier.weight(1f))
//                    Text(
//                        text = orderItem.orderItemStatus.toString()
//                    )
                }

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
                                keyboardState = false
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
                    onClick = { keyboardState = !keyboardState }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Customize the item"
                    )
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
}

// user can customize an order item by add or remove ingredients and addons.
@Composable
fun OrderItemCustomizeSection(
    addedNote: String,
    readOnly: Boolean = false,
    onAddedNoteChange: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(4.dp)
    ) {

        TextField(
            value = addedNote,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            onValueChange = {
                onAddedNoteChange(it)
            },
            placeholder = { Text(text = "Add notes")},
            readOnly = readOnly
        )

/*
        Text(
            text = "Ingredient",
            fontWeight = FontWeight.Bold
        )
         Ingredients:: this section prints the list of ingredients on an item.
        for (ingredient in orderItem.ingredients) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = ingredient.second,
                    onCheckedChange = {
                        onItemChange(orderItem.updateIngredientStatus(ingredient.first, it))
                    })
                Text(text = ingredient.first)
            }
        }


        Text(
            text = "Addons",
            fontWeight = FontWeight.Bold
        )
         Addons:: this section prints the list of addons on an item.
        for (addon in orderItem.addons) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = addon.second,
                    onCheckedChange = {
                        onItemChange(orderItem.updateAddonStatus(addon.first, it))
                    })
                Text(text = addon.first)
            }
        }
 */
    }

}

@Preview(showBackground = true)
@Composable
private fun OrderItemPreview() {
    TableTrackerTheme {
        OrderItemComponent(
            orderItem = OrderItem(
                id = 0,
                menuItem = DummyData.menuItems[0],
                quantity = 1,
                addedNote = "",
                orderId = "",
                orderItemStatus = OrderItemStatus.Added
            ),
            orderType = OrderType.TakeOut,
            onItemRemoveClick = {

            },
            onItemChange = {

            }
        )
    }
}

/*
@Composable
fun OrderListItemComponent(
    item: OrderItem,
    modifier: Modifier = Modifier,
    onQuantityChange: (Int) -> Unit
) {
    var dropDownState by rememberSaveable {
        mutableStateOf(false)
    }
    Column {
        ListItem(
            leadingContent = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Remove item from the order"
                    )
                }
            },
            headlineContent = {
//                Text(
//                    text = item.itemName
//                )
            },
            supportingContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onQuantityChange(item.quantity - 1) }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.remove_circle_outlined),
                            contentDescription = "Decrease Quantity"
                        )

                    }
                    Text(text = item.quantity.toString())
                    IconButton(onClick = { onQuantityChange(item.quantity + 1) }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.add_circle_outline),
                            contentDescription = "Increase Quantity"
                        )
                    }
                }
            },
            trailingContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    Text(text = "£${item.price}")

                    IconButton(
                        onClick = { dropDownState = !dropDownState }
                    ) {
                        Icon(
                            imageVector = if (dropDownState)
                                Icons.Default.KeyboardArrowUp
                            else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Customize the item"
                        )
                    }
                }
            }
        )
        if (dropDownState) {
            OrderItemCustomizeSection(addedNote = "") {

            }
        }
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
        )

    }
}
 */