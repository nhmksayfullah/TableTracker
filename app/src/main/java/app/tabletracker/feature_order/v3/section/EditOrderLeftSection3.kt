package app.tabletracker.feature_order.v3.section

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.tabletracker.feature_order.data.entity.OrderItem
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems
import app.tabletracker.feature_order.v2.component.FoodItemInOrderComponent



@Composable
fun EditOrderLeftSectionContent3(
    runningOrder: OrderWithOrderItems,
    modifier: Modifier = Modifier,
    onItemRemoveClick: (OrderItem) -> Unit = {},
    onItemChange: (OrderItem) -> Unit = {}
) {
    LazyColumn(
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

