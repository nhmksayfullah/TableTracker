package app.tabletracker.feature_order.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import app.tabletracker.core.ui.SplitScreen
import app.tabletracker.feature_order.data.entity.OrderStatus
import app.tabletracker.feature_order.ui.OrderUiEvent
import app.tabletracker.feature_order.ui.OrderViewModel
import app.tabletracker.feature_order.ui.section.CompleteOrderRightDrawer
import app.tabletracker.feature_order.ui.section.ShowMenuRightSection
import app.tabletracker.feature_order.ui.section.ShowOrderLeftSection
import app.tabletracker.util.TableTrackerDefault
import kotlinx.coroutines.launch
import kotlin.random.Random


@Composable
fun TakeOrderScreen(
    modifier: Modifier = Modifier,
    orderId: String,
    orderViewModel: OrderViewModel,
    onOrderDismiss: () -> Unit
) {
    BackHandler(true) {}


    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val orderUiState by orderViewModel.uiState.collectAsState()
    orderViewModel.onEvent(OrderUiEvent.UpdateCurrentOrderWithOrderItems(orderId))

    SplitScreen(
        leftContent = {
            if (orderUiState.currentOrder != null) {
                ShowOrderLeftSection(
                    order = orderUiState.currentOrder!!,
                    readOnly = false,
                    onItemChange = {
                        orderViewModel.onEvent(OrderUiEvent.UpdateOrderItem(it))
                    },
                    onItemRemoveClick = {
                        orderViewModel.onEvent(OrderUiEvent.RemoveItemFromOrder(it))
                    },
                    onPlaceOrderClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onCancelOrderClick = {
                        orderViewModel.onEvent(OrderUiEvent.UpdateCurrentOrder(
                            orderUiState.currentOrder!!.order.copy(orderStatus = OrderStatus.Cancelled))
                        )
                        onOrderDismiss()
                    },
                    onOrderUiEvent = {
                        orderViewModel.onEvent(it)
                    }
                )
            }
        },
        rightContent = {
            if (orderUiState.currentOrder != null) {
                ShowMenuRightSection(
                    menus = orderUiState.menus,
                    onCategoryClicked = {

                    },
                    onMenuItemClicked = {
                        orderViewModel.onEvent(OrderUiEvent.AddItemToOrder(it, orderUiState.currentOrder!!.order.id))
                    }
                )
            }
        },
        drawerState = drawerState,
        drawerContent = {
            CompleteOrderRightDrawer(
                orderUiState = orderUiState,
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