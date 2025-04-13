package app.tabletracker.features.order.ui.screen.runningorder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.tabletracker.features.order.data.entity.OrderWithOrderItems
import app.tabletracker.features.order.ui.component.TableCell

@Composable
fun ShowOrderListRightSection(
    orders: List<OrderWithOrderItems>,
    modifier: Modifier = Modifier,
    onOrderItemClick: (OrderWithOrderItems) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(orders) {
            OrderComponent2(
                order = it,
                modifier = Modifier.padding(2.dp)
            ) {
                onOrderItemClick(it)
            }
        }
    }
}

@Composable
fun OrderComponent2(
    order: OrderWithOrderItems,
    modifier: Modifier = Modifier,
    onClick: (OrderWithOrderItems) -> Unit
) {
    Card(
        modifier = Modifier
            .clickable { onClick(order) }
            .fillMaxWidth()
            .padding(end = 8.dp)
            .then(modifier)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)

        ) {
            TableCell(
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = order.order.orderNumber.toString())
            }
            TableCell {
                Text(text = order.order.orderType.label)
            }
            TableCell {
                Text(text = "${order.order.tableNumber ?: "N/A"}")
            }
            TableCell {
                Text(text = order.order.paymentMethod.name)
            }
            TableCell {
                Text(text = "${order.order.totalPerson ?: "N/A"}")
            }
            TableCell(
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(text = "£%.2f".format(order.order.totalPrice))
            }
        }
    }
}