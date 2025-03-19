package app.tabletracker.feature_order.ui.screen.takeorder

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.tabletracker.core.ui.SplitRatio
import app.tabletracker.core.ui.SplitScreen
import app.tabletracker.feature_order.ui.state.OrderUiEvent
import app.tabletracker.feature_order.ui.state.OrderViewModel
import kotlinx.coroutines.launch

@Composable
fun TakeOrderScreen(
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
            TakeOrderScreenLeftSection(
                orderUiState = uiState,
                onOrderUiEvent = orderViewModel::onEvent,
                onCancelOrder = {
                    orderViewModel.onEvent(it)
                    onOrderDismiss()
                },
                onPlaceOrder = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        },
        rightContent = {
            Surface {
                MenuDisplayRightSection(
                    menus = uiState.menus,
                    onMenuItemClicked = {
                        orderViewModel.onEvent(OrderUiEvent.AddMenuItemToOrder(it))
                    }
                )
            }
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