package app.tabletracker.feature_order.ui.section

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.tabletracker.feature_order.data.entity.OrderStatus
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems
import app.tabletracker.feature_order.ui.component.OrderComponent

@Composable
fun ShowOrderListRightSection(
    orders: List<OrderWithOrderItems>,
    onOrderItemClick: (Int) -> Unit
) {
    if (orders.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(orders) {
                OrderComponent(order = it.order) {order ->
                    onOrderItemClick(orders.indexOf(it))
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No order in ${OrderStatus.Running} order list")
        }
    }
}