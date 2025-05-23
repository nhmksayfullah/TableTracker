package app.tabletracker.features.order.ui.screen.runningorder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.tabletracker.core.ui.SplitRatio
import app.tabletracker.core.ui.SplitScreen
import app.tabletracker.features.order.ui.state.OrderUiEvent
import app.tabletracker.features.order.ui.state.OrderViewModel


@Composable
fun RunningOrderScreen(
    orderViewModel: OrderViewModel,
    modifier: Modifier = Modifier,
    onCustomizeCurrentOrder: () -> Unit
) {

    val uiState by orderViewModel.uiState.collectAsStateWithLifecycle()

    SplitScreen(
        modifier = modifier,
        ratio = SplitRatio(leftWeight = .30f),
        leftContent = {
            if (uiState.currentOrder != null) {
                RunningOrderLeftSection(
                    orderUiState = uiState,
                    onCustomizeCurrentOrder = onCustomizeCurrentOrder
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Select an order to view")
                }
            }
        },
        rightContent = {
            Surface {
                RunningOrderRightSection(
                    orderUiState = uiState,
                    onOrderItemClick = {
                        orderViewModel.onEvent(OrderUiEvent.SetCurrentOrderWithOrderItems(it))
                    }
                )
            }
        }
    )

}