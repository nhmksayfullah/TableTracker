package app.tabletracker.feature_order

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.tabletracker.core.navigation.Screen
import app.tabletracker.feature_order.ui.state.OrderUiEvent
import app.tabletracker.feature_order.ui.state.OrderViewModel
import app.tabletracker.feature_order.ui.screen.startorder.StartOrderScreen
import app.tabletracker.feature_order.ui.screen.runningorder.RunningOrderScreen
import app.tabletracker.feature_order.ui.screen.takeorder.TakeOrderScreen
import app.tabletracker.util.accessSharedViewModel
import kotlinx.serialization.Serializable

@Serializable data object OrderManagementApp

fun NavGraphBuilder.orderManagementNavGraph(
    navController: NavHostController,
) {
    navigation<OrderManagementApp>(
        startDestination = Screen.StartOrderScreen
    ) {
        composable<Screen.StartOrderScreen>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        delayMillis = 400,
                        durationMillis = 1000
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        durationMillis = 1000
                    )
                )
            }
        ) {
            val orderViewModel = it.accessSharedViewModel<OrderViewModel>(navController)
            StartOrderScreen(
                orderViewModel = orderViewModel,
            ) {
                navController.navigate(Screen.TakeOrderScreen)
            }
        }

        composable< Screen.TakeOrderScreen>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        delayMillis = 500,
                        durationMillis = 1000
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        durationMillis = 1000
                    )
                )
            }
        ) {
            val orderViewModel = it.accessSharedViewModel<OrderViewModel>(navController)

            TakeOrderScreen(
                orderViewModel = orderViewModel,
                onOrderDismiss = {
                    orderViewModel.onEvent(OrderUiEvent.SetCurrentOrderWithOrderItems(null))
                    navController.navigateUp()
                }
            )
        }

        composable<Screen.RunningOrderScreen>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        delayMillis = 400,
                        durationMillis = 1000
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        durationMillis = 1000
                    )
                )
            }
        )  {
            val orderViewModel = it.accessSharedViewModel<OrderViewModel>(navController)
            RunningOrderScreen(
                orderViewModel = orderViewModel,
                onCustomizeCurrentOrder = {
                    navController.navigate(Screen.TakeOrderScreen)
                }
            )
        }
    }
}