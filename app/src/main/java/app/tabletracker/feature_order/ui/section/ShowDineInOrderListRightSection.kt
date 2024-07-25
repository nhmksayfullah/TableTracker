package app.tabletracker.feature_order.ui.section

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.tabletracker.core.ui.component.FoodBlockComponent
import app.tabletracker.core.ui.component.TextBoxComponent
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems
import app.tabletracker.theme.MaterialBlue
import app.tabletracker.theme.MaterialRed

@Composable
fun ShowDineInOrderListRightSection(
    modifier: Modifier = Modifier,
    totalTable: Int,
    orders: List<OrderWithOrderItems>,
    onOrderItemClick: (Int) -> Unit
) {
    val availableTables = orders.map { it.order.tableNumber }
    val tableOrders = orders.filter { it.order.tableNumber != null }.sortedBy {
        it.order.tableNumber
    }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        for (i in 1..totalTable) {
            item {
                Column(modifier = Modifier
                    .padding(4.dp)) {
                    FoodBlockComponent(
                        text = i.toString(),
                        containerColor = if (i in availableTables) MaterialRed else MaterialBlue
                    ) {
                        if (i in availableTables) {
                            val order = orders.find {
                                it.order.tableNumber == i
                            }.let {
                                onOrderItemClick(orders.indexOf(it))
                            }
                        } else {
                            onOrderItemClick(-1)
                        }
                    }

                }
            }
        }
    }
}