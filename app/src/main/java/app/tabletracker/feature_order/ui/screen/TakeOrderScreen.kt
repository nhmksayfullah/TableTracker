package app.tabletracker.feature_order.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.tabletracker.core.ui.SplitScreen
import app.tabletracker.feature_order.data.entity.OrderStatus
import app.tabletracker.feature_order.ui.OrderUiEvent
import app.tabletracker.feature_order.ui.OrderViewModel
import app.tabletracker.feature_order.ui.section.CompleteOrderDrawer
import app.tabletracker.feature_order.ui.section.SelectCategoryRightSection
import app.tabletracker.feature_order.ui.section.SelectMenuItemRightSection
import app.tabletracker.feature_order.ui.section.ShowOrderLeftSection
import kotlinx.coroutines.launch


@Composable
fun TakeOrderScreen(
    modifier: Modifier = Modifier,
    orderId: String,
    orderViewModel: OrderViewModel,
    onOrderDismiss: () -> Unit
) {

    var isCategoryVisible by rememberSaveable {
        mutableStateOf(true)
    }
    BackHandler(true) {
        isCategoryVisible = true
    }

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val orderUiState by orderViewModel.uiState.collectAsState()
    orderViewModel.onEvent(OrderUiEvent.UpdateCurrentOrderWithOrderItems(orderId))

    var selectedCategoryIndex by rememberSaveable {
        if (orderUiState.menus.isEmpty()) mutableIntStateOf(0) else mutableIntStateOf((orderUiState.menus[0].category.id - 1))
    }
    var selectedCategoryId by rememberSaveable {
        mutableIntStateOf(-1)
    }

    SplitScreen(
        leftContent = {
            if (orderUiState.currentOrder != null) {
                ShowOrderLeftSection(
                    orderUiState = orderUiState,
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
//                ShowMenuRightSection(
//                    menus = orderUiState.menus,
//                    onCategoryClicked = {
//
//                    },
//                    onMenuItemClicked = {
//                        orderViewModel.onEvent(OrderUiEvent.AddItemToOrder(it, orderUiState.currentOrder!!.order.id))
//                    }
//                )
                if (isCategoryVisible) {
                    SelectCategoryRightSection(
                        menus = orderUiState.menus.sortedBy { it.category.index },
                        onCategoryClicked = {
                            isCategoryVisible = false
                            selectedCategoryIndex = it.id - 1
                            selectedCategoryId = it.id
                        }
                    )
                } else {
                    orderUiState.menus.find {
                        it.category.id == selectedCategoryId
                    }?.let {
                        if (it.menuItems.isNotEmpty()) {
                            SelectMenuItemRightSection(
                                menus = it.menuItems,
                                onMenuItemClicked = {
                                    orderViewModel.onEvent(OrderUiEvent.AddMenuItemToOrder(it))
                                }
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "No Menu Items Available")
                            }
                        }
                    }

                }
            }
        },
        drawerState = drawerState,
        drawerContent = {
            CompleteOrderDrawer(
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