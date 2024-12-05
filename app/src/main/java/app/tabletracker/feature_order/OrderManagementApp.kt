package app.tabletracker.feature_order

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import app.tabletracker.app.ui.AppUiEvent
import app.tabletracker.core.navigation.Screen
import app.tabletracker.core.navigation.Applications
import app.tabletracker.feature_order.ui.OrderUiEvent
import app.tabletracker.feature_order.ui.OrderViewModel
import app.tabletracker.feature_order.ui.screen.RunningOrderScreen
import app.tabletracker.feature_order.ui.screen.StartOrderScreen
import app.tabletracker.feature_order.ui.screen.TakeOrderScreen
import app.tabletracker.util.accessSharedViewModel

fun NavGraphBuilder.orderManagementApp(
    navController: NavHostController,
    onAppUiEvent: (AppUiEvent) -> Unit
) {
    navigation(
        route = Applications.OrderManagementApp.route,
        startDestination = Screen.StartOrderScreen.route
    ) {

        composable(Screen.StartOrderScreen.route) {
            val orderViewModel = it.accessSharedViewModel<OrderViewModel>(navController)
            StartOrderScreen(
                orderViewModel = orderViewModel
            ) {orderId ->
                onAppUiEvent(AppUiEvent.ChangeScreen(Screen.TakeOrderScreen))
                navController.navigate(Screen.TakeOrderScreen.withArgs(orderId))
            }

        }

        composable(
            route = Screen.TakeOrderScreen.route + "/{orderId}",
            arguments = listOf(navArgument("orderId") {
                type = NavType.StringType
            })
        ) {
            val orderViewModel = it.accessSharedViewModel<OrderViewModel>(navController)
            val orderId = it.arguments?.getString("orderId")
            if (orderId != null) {
                TakeOrderScreen(
                    orderId = orderId,
                    orderViewModel = orderViewModel,
                    onOrderDismiss = {
                        orderViewModel.onEvent(OrderUiEvent.UpdateCurrentOrderWithNull)
                        onAppUiEvent(AppUiEvent.ChangeScreen(Screen.StartOrderScreen))
                        navController.navigate(Screen.StartOrderScreen.route)
                    }
                )
            }
        }

        composable(route = Screen.RunningOrderScreen.route) {
            val orderViewModel = it.accessSharedViewModel<OrderViewModel>(navController)
            RunningOrderScreen(
                orderViewModel = orderViewModel,
                onCustomizeOrderClick = {orderId ->
                    navController.navigate(Screen.TakeOrderScreen.withArgs(orderId))
                    onAppUiEvent(AppUiEvent.ChangeScreen(Screen.TakeOrderScreen))
                }
            )
        }
    }
}