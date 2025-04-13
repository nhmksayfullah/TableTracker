package app.tabletracker.features.order.ui.screen.runningorder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.tabletracker.features.order.data.entity.OrderWithOrderItems
import app.tabletracker.theme.MaterialColor

@Composable
fun RunningOrderTableView(
    totalTable: Int,
    runningOrders: List<OrderWithOrderItems>,
    modifier: Modifier = Modifier,
    onOrderItemClick: (OrderWithOrderItems) -> Unit
) {
    val bookedTables = runningOrders.map { it.order.tableNumber }
    val ordersInTable = runningOrders.filter { it.order.tableNumber != null }.sortedBy {
        it.order.tableNumber
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Table View",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.weight(1f))
            Card(
                modifier = Modifier
                    .size(16.dp),
                colors = CardDefaults.cardColors().copy(
                    containerColor = MaterialColor.Green.color
                )
            ) { }
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Available",
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(Modifier.width(16.dp))
            Card(
                modifier = Modifier
                    .size(16.dp),
                colors = CardDefaults.cardColors().copy(
                    containerColor = MaterialColor.PureRed.color
                )
            ) { }
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Booked",
                style = MaterialTheme.typography.labelMedium
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Adaptive(120.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            for (i in 1..totalTable) {
                item {
                    Column(
                        modifier = Modifier
                            .padding(2.dp)
                    ) {
                        TableBoxComponent(
                            tableNumber = i,
                            booked = i in bookedTables,
                            onTableClick = {
                                ordersInTable.find {
                                    it.order.tableNumber == i
                                }?.let {
                                    onOrderItemClick(it)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun TableBoxComponent(
    tableNumber: Int,
    booked: Boolean,
    modifier: Modifier = Modifier,
    onTableClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = if (booked) MaterialColor.PureRed.color else MaterialColor.Green.color,
            contentColor = Color.White
        ),
        modifier = Modifier
            .wrapContentSize()
            .clickable { onTableClick() }
            .then(modifier)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            Text(
                text = tableNumber.toString(),
                maxLines = 2,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
