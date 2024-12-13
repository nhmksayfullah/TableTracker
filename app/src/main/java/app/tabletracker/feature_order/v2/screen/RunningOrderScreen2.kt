package app.tabletracker.feature_order.v2.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.tabletracker.core.ui.SplitRatio
import app.tabletracker.core.ui.SplitScreen
import app.tabletracker.feature_order.v2.section.left.RunningOrderLeftSection
import app.tabletracker.feature_order.v2.section.right.RunningOrderRightSection
import app.tabletracker.feature_order.v2.state.OrderUiEvent2
import app.tabletracker.feature_order.v2.state.OrderViewModel2

@Composable
fun RunningOrderScreen2(
    orderViewModel: OrderViewModel2,
    modifier: Modifier = Modifier,
    onCustomizeCurrentOrder: () -> Unit
) {
    val uiState by orderViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

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
            RunningOrderRightSection(
                orderUiState = uiState,
                onOrderItemClick = {
                    orderViewModel.onEvent(OrderUiEvent2.SetCurrentOrderWithOrderItems(it))
                }
            )
        }
    )

}