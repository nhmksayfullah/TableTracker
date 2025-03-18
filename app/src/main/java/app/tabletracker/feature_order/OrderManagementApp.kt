package app.tabletracker.feature_order

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.tabletracker.app.ui.AppUiEvent
import app.tabletracker.core.navigation.Screen
import app.tabletracker.feature_order.ui.state.OrderUiEvent
import app.tabletracker.feature_order.ui.state.OrderViewModel
import app.tabletracker.feature_order.ui.screen.StartOrderScreen
import app.tabletracker.feature_order.ui.screen.RunningOrderScreen3
import app.tabletracker.feature_order.ui.screen.TakeOrderScreen
import app.tabletracker.util.accessSharedViewModel
import kotlinx.serialization.Serializable

@Serializable data object OrderManagementApp2
@Serializable data object StartOrderScreen
@Serializable data class TakeOrderScreen(val orderId: String?, val isNewOrder: Boolean)
@Serializable data object RunningOrderScreen

fun NavGraphBuilder.orderManagementGraph(
    navController: NavHostController,
    onAppUiEvent: (AppUiEvent) -> Unit
) {
    navigation<OrderManagementApp2>(
        startDestination = StartOrderScreen
    ) {
        composable<StartOrderScreen> {
            val orderViewModel = it.accessSharedViewModel<OrderViewModel>(navController)
            StartOrderScreen(
                orderViewModel = orderViewModel,
            ) {
                onAppUiEvent(AppUiEvent.ChangeScreen(Screen.TakeOrderScreen))
                navController.navigate(TakeOrderScreen(orderId = "", isNewOrder = true))
            }
        }

        composable<TakeOrderScreen> {
            val orderViewModel = it.accessSharedViewModel<OrderViewModel>(navController)

            TakeOrderScreen(
                orderViewModel = orderViewModel,
                onOrderDismiss = {
                    onAppUiEvent(AppUiEvent.ChangeScreen(Screen.StartOrderScreen))
                    orderViewModel.onEvent(OrderUiEvent.SetCurrentOrderWithOrderItems(null))
                    navController.navigateUp()
                }
            )
        }

        composable<RunningOrderScreen> {
            val orderViewModel = it.accessSharedViewModel<OrderViewModel>(navController)
            RunningOrderScreen3(
                orderViewModel = orderViewModel,
                onCustomizeCurrentOrder = {
                    onAppUiEvent(AppUiEvent.ChangeScreen(Screen.TakeOrderScreen))
                    navController.navigate(TakeOrderScreen(orderId = "", isNewOrder = false))
                }
            )
        }
    }
}