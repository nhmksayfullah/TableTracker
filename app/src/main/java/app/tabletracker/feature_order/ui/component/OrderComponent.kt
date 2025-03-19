package app.tabletracker.feature_order.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.tabletracker.feature_order.data.entity.Order

//val id: Int = 0,
//val orderType: OrderType,
//val tableNumber: Int? = null,
//val paymentMethod: PaymentMethod? = null,
//val orderStatus: OrderStatus = OrderStatus.Created,
//val totalPrice: Float = 0.0f

@Composable
fun ShowOrderColumnNamesOnRightSection() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        TableCell {
            Text(
                text = "Order No.",
                style = MaterialTheme.typography.titleSmall,
            )
        }
        TableCell {
            Text(
                text = "Order Type",
                style = MaterialTheme.typography.titleSmall,
            )
        }
        TableCell {
            Text(
                text = "Table Number",
                style = MaterialTheme.typography.titleSmall,
            )
        }
        TableCell {
            Text(
                text = "Payment Method",
                style = MaterialTheme.typography.titleSmall,
            )
        }
        TableCell {
            Text(
                text = "Total Person",
                style = MaterialTheme.typography.titleSmall,
            )
        }
        TableCell {
            Text(
                text = "Total Price",
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }

}
@Composable
fun OrderComponent(
    order: Order,
    onClick: (Order) -> Unit
) {
    Card(
        modifier = Modifier
            .clickable { onClick(order) }
            .fillMaxWidth()
            .padding(end = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)

        ) {
            TableCell(
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = order.orderNumber.toString())
            }
            TableCell {
                Text(text = order.orderType.label)
            }
            TableCell {
                Text(text = "${order.tableNumber ?: "N/A"}")
            }
            TableCell {
                Text(text = order.paymentMethod.name)
            }
            TableCell {
                Text(text = "${order.totalPerson ?: "N/A"}")
            }
            TableCell(
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(text = "£${String.format("%.2f", order.totalPrice)}")
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    weight: Float = 1f,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.weight(weight),
        contentAlignment = contentAlignment
    ) {
        content()
    }
}