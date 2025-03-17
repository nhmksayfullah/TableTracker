package app.tabletracker.feature_order.v3.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.tabletracker.core.ui.SplitRatio
import app.tabletracker.core.ui.SplitScreen
import app.tabletracker.feature_order.ui.OrderUiEvent
import app.tabletracker.feature_order.ui.OrderViewModel
import app.tabletracker.feature_order.v2.section.left.RunningOrderLeftSection
import app.tabletracker.feature_order.v2.section.right.RunningOrderRightSection
import app.tabletracker.feature_order.v3.section.RunningOrderLeftSection3


@Composable
fun RunningOrderScreen3(
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
                RunningOrderLeftSection3(
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
            RunningOrderRightSection(
                orderUiState = uiState,
                onOrderItemClick = {
                    orderViewModel.onEvent(OrderUiEvent.SetCurrentOrderWithOrderItems(it))
                }
            )
        }
    )

}