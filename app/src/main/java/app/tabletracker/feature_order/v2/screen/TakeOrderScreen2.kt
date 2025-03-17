package app.tabletracker.feature_order.v2.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.tabletracker.core.ui.SplitRatio
import app.tabletracker.core.ui.SplitScreen
import app.tabletracker.feature_order.v2.section.left.TakeOrderLeftSection
import app.tabletracker.feature_order.v2.section.right.ShowMenuRightSection2
import app.tabletracker.feature_order.v2.state.OrderUiEvent2
import app.tabletracker.feature_order.v2.state.OrderViewModel2

@Composable
fun TakeOrderScreen2(
    orderViewModel: OrderViewModel2,
    modifier: Modifier = Modifier,
    onOrderDismiss: () -> Unit
) {
    val uiState by orderViewModel.uiState.collectAsStateWithLifecycle()
    SplitScreen(
        ratio = SplitRatio(leftWeight = .35f),
        leftContent = {
            TakeOrderLeftSection(
                orderUiState = uiState,
                onOrderUiEvent = orderViewModel::onEvent,
                onOrderDismiss = onOrderDismiss
            )

        },
        rightContent = {
            ShowMenuRightSection2(
                menus = uiState.menus,
                onMenuItemClicked = {
                    orderViewModel.onEvent(OrderUiEvent2.AddMenuItemToOrder(it))
                }
            )
        }
    )
}