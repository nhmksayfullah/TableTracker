package app.tabletracker.feature_order.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.tabletracker.R
import app.tabletracker.feature_order.data.entity.OrderItem
import app.tabletracker.feature_order.data.entity.OrderType

@Composable
fun FoodItemInOrderComponent(
    item: OrderItem,
    orderType: OrderType,
    modifier: Modifier = Modifier,
    onItemRemoveClick: () -> Unit,
    onItemChange: (OrderItem) -> Unit
) {

    val unitPrice = item.menuItem.prices[orderType] ?: 0.0f
    FoodItem(
        foodName = item.menuItem.name,
        price = "%.2f".format(unitPrice.times(item.quantity)),
        quantity = item.quantity,
        addedNote = item.addedNote,
        onPlusClick = {
            onItemChange(item.copy(quantity = item.quantity + 1))
        },
        onMinusClick = {
            if (item.quantity == 1) {
                onItemRemoveClick()
            } else {
                onItemChange(item.copy(quantity = item.quantity - 1))
            }
        },
        onRemoveClick = onItemRemoveClick,
        onNoteAdded = {
            onItemChange(item.copy(addedNote = it))
        }
    )

}

@Composable
fun FoodItem(
    foodName: String,
    price: String,
    quantity: Int,
    addedNote: String,
    modifier: Modifier = Modifier,
    onPlusClick: () -> Unit,
    onMinusClick: () -> Unit,
    onRemoveClick: () -> Unit,
    onNoteAdded: (note: String) -> Unit
) {
    var showKeyboard by rememberSaveable {
        mutableStateOf(false)
    }
    var _addedNote by rememberSaveable {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = addedNote) {
        _addedNote = addedNote
    }
    Column(
        modifier = modifier
            .padding(vertical = 4.dp)
            .then(modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = onRemoveClick
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Remove item from the order",
                    tint = MaterialTheme.colorScheme.error
                )
            }

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentSize(Alignment.CenterStart)
            ) {
                Text(
                    text = foodName,
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .basicMarquee()
                )
                Text(
                    text = "£$price",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = onMinusClick
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.remove_circle_outlined),
                        contentDescription = "Decrease Quantity"
                    )
                }
                Text(text = quantity.toString())
                IconButton(
                    onClick = onPlusClick
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.add_circle_outline),
                        contentDescription = "Increase Quantity"
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (showKeyboard) {
                    IconButton(
                        onClick = {
                            onNoteAdded(_addedNote)
                            showKeyboard = false
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Customize the item"
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            showKeyboard = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Customize the item"
                        )
                    }
                }
            }
        }
        AnimatedVisibility(showKeyboard) {
            TextField(
                value = _addedNote,
                onValueChange = {
                    _addedNote = it
                },
                label = {
                    Text(text = "Note")
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
            )
        }
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(MaterialTheme.colorScheme.inversePrimary)
        )
    }
}

@Preview
@Composable
private fun ItemComponent() {
    FoodItem(
        foodName = "Pizza",
        price = "10.00",
        quantity = 1,
        addedNote = "Extra Cheese",
        onPlusClick = {},
        onMinusClick = {},
        onRemoveClick = {},
        onNoteAdded = {}
    )
}