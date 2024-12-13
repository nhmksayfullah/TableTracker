package app.tabletracker.feature_order.v2.section.left

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.tabletracker.feature_order.data.entity.OrderItem
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems
import app.tabletracker.feature_order.v2.component.FoodItemInOrderComponent

@Composable
fun EditOrderLeftSectionHeader(
    orderNumber: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        Text("Order No: $orderNumber")
    }
}

@Composable
fun EditOrderLeftSectionContent(
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

