package app.tabletracker.feature_order.v2

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import app.tabletracker.app.ui.AppUiEvent
import app.tabletracker.core.navigation.Screen
import app.tabletracker.feature_order.v2.screen.RunningOrderScreen2
import app.tabletracker.feature_order.v2.screen.StartOrderScreen2
import app.tabletracker.feature_order.v2.screen.TakeOrderScreen2
import app.tabletracker.feature_order.v2.state.OrderUiEvent2
import app.tabletracker.feature_order.v2.state.OrderViewModel2
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
            val orderViewModel = it.accessSharedViewModel<OrderViewModel2>(navController)
            StartOrderScreen2(
                orderViewModel = orderViewModel,
            ) {
                onAppUiEvent(AppUiEvent.ChangeScreen(Screen.TakeOrderScreen))
                navController.navigate(TakeOrderScreen(orderId = "", isNewOrder = true))
            }
        }

        composable<TakeOrderScreen> {
            val orderViewModel = it.accessSharedViewModel<OrderViewModel2>(navController)
            val arguments = it.toRoute<TakeOrderScreen>()
            val isNewOrder = arguments.isNewOrder

            TakeOrderScreen2(
                orderViewModel = orderViewModel,
                onOrderDismiss = {
                    onAppUiEvent(AppUiEvent.ChangeScreen(Screen.StartOrderScreen))
                    orderViewModel.onEvent(OrderUiEvent2.SetCurrentOrderWithOrderItems(null))
                    navController.navigateUp()
                }
            )
        }

        composable<RunningOrderScreen> {
            val orderViewModel = it.accessSharedViewModel<OrderViewModel2>(navController)
            RunningOrderScreen2(
                orderViewModel = orderViewModel,
                onCustomizeCurrentOrder = {
                    onAppUiEvent(AppUiEvent.ChangeScreen(Screen.TakeOrderScreen))
                    navController.navigate(TakeOrderScreen(orderId = "", isNewOrder = false))
                }
            )
        }
    }
}