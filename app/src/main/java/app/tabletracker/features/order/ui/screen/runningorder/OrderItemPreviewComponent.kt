package app.tabletracker.features.order.ui.screen.runningorder

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.tabletracker.R
import app.tabletracker.features.order.data.entity.OrderItem
import app.tabletracker.features.order.data.entity.OrderWithOrderItems
import app.tabletracker.features.order.ui.component.FoodItemInOrderComponent


@Composable
fun OrderDetailsPreviewLeftSection(
    runningOrder: OrderWithOrderItems,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(runningOrder.orderItems) {
            val unitPrice = it.menuItem.prices[runningOrder.order.orderType] ?: 0.0f
            PreviewFoodItem(
                foodName = it.menuItem.name,
                price = unitPrice.toString(),
                quantity = it.quantity,
                addedNote = it.addedNote,
                modifier = Modifier.padding(4.dp),
            )
        }
    }
}

@Composable
fun PreviewFoodItem(
    foodName: String,
    price: String,
    quantity: Int,
    addedNote: String,
    modifier: Modifier = Modifier,
) {
    var showKeyboard by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = foodName,
                overflow = TextOverflow.Clip,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
                    .basicMarquee()
            )
            Spacer(Modifier.width(8.dp))

            Text(
                text = "x$quantity",
                color = MaterialTheme.colorScheme.error,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "£$price",
                style = MaterialTheme.typography.bodyMedium
            )
            TextButton(
                onClick = {
                    showKeyboard = !showKeyboard
                }
            ) {
                Text("Show Note")
            }
        }
        AnimatedVisibility(showKeyboard) {
            TextField(
                value = addedNote,
                onValueChange = {},
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
