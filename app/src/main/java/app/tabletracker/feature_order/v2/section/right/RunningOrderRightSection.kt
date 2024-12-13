package app.tabletracker.feature_order.v2.section.right

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import app.tabletracker.core.ui.TabbedScreen
import app.tabletracker.feature_order.data.entity.OrderStatus
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems
import app.tabletracker.feature_order.ui.component.ShowOrderColumnNamesOnRightSection
import app.tabletracker.feature_order.v2.state.OrderUiState2

@Composable
fun RunningOrderRightSection(
    orderUiState: OrderUiState2,
    modifier: Modifier = Modifier,
    onOrderItemClick: (OrderWithOrderItems) -> Unit
) {

    val orderStatusList = listOf(
        OrderStatus.Running.name,
        OrderStatus.Completed.name,
        OrderStatus.Cancelled.name
    )

    TabbedScreen(
        modifier = modifier,
        titles = orderStatusList,
        onClick = {  }
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        when (OrderStatus.valueOf(orderStatusList[it])) {
            OrderStatus.Created -> {}
            OrderStatus.Running -> {
                Column {
//                    orderUiState.restaurantExtra?.let {
//
//                    }
                    RunningOrderTableView(
                        totalTable = orderUiState.restaurantExtra?.totalTable ?: 0,
                        runningOrders = orderUiState.runningOrders,
                        onOrderItemClick = onOrderItemClick
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ShowOrderColumnNamesOnRightSection()
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(
                        modifier = Modifier
                            .height(1.dp)
                            .padding(end = 16.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ShowOrderListRightSection2(
                        orders = orderUiState.runningOrders.filter { it.order.tableNumber == null },
                        onOrderItemClick = onOrderItemClick
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            OrderStatus.Completed -> {
                ShowOrderColumnNamesOnRightSection()
                Spacer(modifier = Modifier.height(8.dp))
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .padding(end = 16.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.height(8.dp))
                ShowOrderListRightSection2(
                    orders = orderUiState.completedOrders,
                    onOrderItemClick = onOrderItemClick
                )
            }
            OrderStatus.Cancelled -> {
                ShowOrderColumnNamesOnRightSection()
                Spacer(modifier = Modifier.height(8.dp))
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .padding(end = 16.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.height(8.dp))
                ShowOrderListRightSection2(
                    orders = orderUiState.cancelledOrders,
                    onOrderItemClick = onOrderItemClick
                )
            }
        }
    }
}