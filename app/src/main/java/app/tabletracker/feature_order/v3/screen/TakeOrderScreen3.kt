package app.tabletracker.feature_order.v3.screen

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.tabletracker.core.ui.SplitRatio
import app.tabletracker.core.ui.SplitScreen
import app.tabletracker.feature_order.ui.OrderUiEvent
import app.tabletracker.feature_order.ui.OrderViewModel
import app.tabletracker.feature_order.ui.section.CompleteOrderDrawer
import app.tabletracker.feature_order.v2.section.left.TakeOrderLeftSection
import app.tabletracker.feature_order.v2.section.right.ShowMenuRightSection2
import app.tabletracker.feature_order.v2.state.OrderUiEvent2
import app.tabletracker.feature_order.v2.state.OrderViewModel2
import app.tabletracker.feature_order.v3.section.TakeOrderLeftSection3
import kotlinx.coroutines.launch

@Composable
fun TakeOrderScreen3(
    orderViewModel: OrderViewModel,
    modifier: Modifier = Modifier,
    onOrderDismiss: () -> Unit
) {
    val uiState by orderViewModel.uiState.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    SplitScreen(
        modifier = modifier,
        ratio = SplitRatio(leftWeight = .3f),
        leftContent = {
            TakeOrderLeftSection3(
                orderUiState = uiState,
                onOrderUiEvent = orderViewModel::onEvent,
                onOrderDismiss = onOrderDismiss,
                onPlaceOrder = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        },
        rightContent = {
            ShowMenuRightSection2(
                menus = uiState.menus,
                onMenuItemClicked = {
                    orderViewModel.onEvent(OrderUiEvent.AddMenuItemToOrder(it))
                }
            )
        },
        drawerState = drawerState,
        drawerContent = {
            CompleteOrderDrawer(
                orderUiState = uiState,
                onOrderDismiss = {
                    if (it != null) orderViewModel.onEvent(it)
                    scope.launch {
                        drawerState.close()
                        onOrderDismiss()
                    }
                },
                orderUiEvent = {
                    orderViewModel.onEvent(it)
                }
            )
        }

    )
}