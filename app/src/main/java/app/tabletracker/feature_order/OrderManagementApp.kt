package app.tabletracker.feature_order

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.tabletracker.core.navigation.Screen
import app.tabletracker.feature_order.ui.state.OrderUiEvent
import app.tabletracker.feature_order.ui.state.OrderViewModel
import app.tabletracker.feature_order.ui.screen.StartOrderScreen
import app.tabletracker.feature_order.ui.screen.RunningOrderScreen3
import app.tabletracker.feature_order.ui.screen.TakeOrderScreen
import app.tabletracker.util.accessSharedViewModel
import kotlinx.serialization.Serializable

@Serializable data object OrderManagementApp

fun NavGraphBuilder.orderManagementNavGraph(
    navController: NavHostController,
) {
    navigation<OrderManagementApp>(
        startDestination = Screen.StartOrderScreen
    ) {
        composable<Screen.StartOrderScreen> {
            val orderViewModel = it.accessSharedViewModel<OrderViewModel>(navController)
            StartOrderScreen(
                orderViewModel = orderViewModel,
            ) {
                navController.navigate(Screen.TakeOrderScreen)
            }
        }

        composable< Screen.TakeOrderScreen> {
            val orderViewModel = it.accessSharedViewModel<OrderViewModel>(navController)

            TakeOrderScreen(
                orderViewModel = orderViewModel,
                onOrderDismiss = {
                    orderViewModel.onEvent(OrderUiEvent.SetCurrentOrderWithOrderItems(null))
                    navController.navigateUp()
                }
            )
        }

        composable<Screen.RunningOrderScreen> {
            val orderViewModel = it.accessSharedViewModel<OrderViewModel>(navController)
            RunningOrderScreen3(
                orderViewModel = orderViewModel,
                onCustomizeCurrentOrder = {
                    navController.navigate(Screen.TakeOrderScreen)
                }
            )
        }
    }
}