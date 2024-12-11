package app.tabletracker.feature_order.v2.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.tabletracker.feature_order.data.entity.OrderItem
import app.tabletracker.R
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
        price = "£%.2f".format(unitPrice.times(item.quantity)),
        quantity = item.quantity,
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
        onRemoveClick = onItemRemoveClick
    )
}

@Composable
fun FoodItem(
    foodName: String,
    price: String,
    quantity: Int,
    modifier: Modifier = Modifier,
    onPlusClick: () -> Unit,
    onMinusClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(vertical = 4.dp)
            .then(modifier)
    ) {
        Row( verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onRemoveClick
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
                Text(foodName)
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
            }
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "£$price")
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Customize the item"
                    )
                }
            }
        }

    }
}

@Preview
@Composable
private fun ItemComponent() {
    FoodItem(
        foodName = "Pizza",
        price = "10.00",
        quantity = 1,
        onPlusClick = {},
        onMinusClick = {},
    ) { }
}