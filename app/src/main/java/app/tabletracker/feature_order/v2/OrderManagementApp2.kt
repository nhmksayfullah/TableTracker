package app.tabletracker.feature_order.v2

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.tabletracker.app.ui.AppUiEvent
import app.tabletracker.core.navigation.Screen
import app.tabletracker.feature_order.ui.OrderUiEvent
import app.tabletracker.feature_order.ui.OrderViewModel
import app.tabletracker.feature_order.v2.screen.RunningOrderScreen2
import app.tabletracker.feature_order.v2.screen.StartOrderScreen2
import app.tabletracker.feature_order.v2.state.OrderViewModel2
import app.tabletracker.feature_order.v3.screen.RunningOrderScreen3
import app.tabletracker.feature_order.v3.screen.TakeOrderScreen3
import app.tabletracker.util.accessSharedViewModel
import kotlinx.serialization.Serializable

@Serializable data object OrderManagementApp2
@Serializable data object StartOrderScreen
@Serializable data class TakeOrderScreen(val orderId: String?, val isNewOrder: Boolean)
@Serializable data object RunningOrderScreen

fun NavGraphBuilder.setupOrderManagementApp2(
    navController: NavHostController,
    onAppUiEvent: (AppUiEvent) -> Unit
) {
    navigation<OrderManagementApp2>(
        startDestination = StartOrderScreen
    ) {
        composable<StartOrderScreen> {
            val orderViewModel = it.accessSharedViewModel<OrderViewModel>(navController)
            StartOrderScreen2(
                orderViewModel = orderViewModel,
            ) {
                onAppUiEvent(AppUiEvent.ChangeScreen(Screen.TakeOrderScreen))
                navController.navigate(TakeOrderScreen(orderId = "", isNewOrder = true))
            }
        }

        composable<TakeOrderScreen> {
            val orderViewModel = it.accessSharedViewModel<OrderViewModel>(navController)

            TakeOrderScreen3(
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