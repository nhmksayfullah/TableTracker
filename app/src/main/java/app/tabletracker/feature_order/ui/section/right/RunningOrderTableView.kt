package app.tabletracker.feature_order.ui.section.right

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems
import app.tabletracker.theme.MaterialBlue
import app.tabletracker.theme.MaterialRed

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
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(120.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        for (i in 1..totalTable) {
            item {
                Column(modifier = Modifier
                    .padding(4.dp)) {
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

@Composable
fun TableBoxComponent(
    tableNumber: Int,
    booked: Boolean,
    modifier: Modifier = Modifier,
    onTableClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = if(booked) MaterialRed else MaterialBlue
        ),
        modifier = Modifier
            .wrapContentSize()
            .clickable{onTableClick()}
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
