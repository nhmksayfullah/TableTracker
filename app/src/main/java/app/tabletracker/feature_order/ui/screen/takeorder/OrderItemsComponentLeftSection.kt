package app.tabletracker.feature_order.ui.screen.takeorder

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import app.tabletracker.feature_order.data.entity.OrderItem
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems
import app.tabletracker.feature_order.ui.component.FoodItemInOrderComponent



@Composable
fun OrderItemsComponentLeftSection(
    runningOrder: OrderWithOrderItems,
    modifier: Modifier = Modifier,
    onItemRemoveClick: (OrderItem) -> Unit = {},
    onItemChange: (OrderItem) -> Unit = {}
) {
    val scrollState = rememberLazyListState()
    LaunchedEffect(key1 = runningOrder.orderItems.size) {
        if (runningOrder.orderItems.isNotEmpty()) {
            scrollState.animateScrollToItem(runningOrder.orderItems.size - 1)
        }
    }
    LazyColumn(
        state = scrollState,
        modifier = modifier
    ) {
        items(runningOrder.orderItems) {
            FoodItemInOrderComponent(
                item = it,
                orderType = runningOrder.order.orderType,
                onItemRemoveClick = {onItemRemoveClick(it)},
                onItemChange = {onItemChange(it)}
            )
        }
    }
}

