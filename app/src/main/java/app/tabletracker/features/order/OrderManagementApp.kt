package app.tabletracker.features.order

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.tabletracker.features.order.ui.screen.runningorder.RunningOrderScreen
import app.tabletracker.features.order.ui.screen.startorder.StartOrderScreen
import app.tabletracker.features.order.ui.screen.takeorder.TakeOrderScreen
import app.tabletracker.features.order.ui.state.OrderViewModel
import app.tabletracker.navigation.Screen
import app.tabletracker.di.accessSharedViewModel
import kotlinx.serialization.Serializable

@Serializable
data object OrderManagementApp

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
                        delayMillis = 250,
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
                onConnectWithCompanion = {}
            ) {
                navController.navigate(Screen.TakeOrderScreen)
            }
        }

        composable<Screen.TakeOrderScreen>(
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

                    navController.navigateUp()
                }
            )
        }

        composable<Screen.RunningOrderScreen>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        delayMillis = 250,
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
            RunningOrderScreen(
                orderViewModel = orderViewModel,
                onCustomizeCurrentOrder = {
                    navController.navigate(Screen.TakeOrderScreen)
                }
            )
        }
    }
}