package app.tabletracker.feature_order.v2

import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import app.tabletracker.app.ui.AppUiEvent
import app.tabletracker.feature_order.ui.OrderUiEvent
import app.tabletracker.feature_order.ui.OrderViewModel
import app.tabletracker.feature_order.v2.screen.RunningOrderScreen2
import app.tabletracker.feature_order.v2.screen.StartOrderScreen2
import app.tabletracker.feature_order.v2.screen.TakeOrderScreen2
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
                navController.navigate(TakeOrderScreen("", true))
            }
        }

        composable<TakeOrderScreen> {
            val orderViewModel = it.accessSharedViewModel<OrderViewModel>(navController)
            val arguments = it.toRoute<TakeOrderScreen>()
            val isNewOrder = arguments.isNewOrder
            val context = LocalContext.current
            LaunchedEffect(isNewOrder) {
                Toast.makeText( context, "Hello", Toast.LENGTH_SHORT).show()
                if (isNewOrder) {
                    orderViewModel.onEvent(OrderUiEvent.PopulateLatestOrder)
                }
            }

            TakeOrderScreen2(
                orderViewModel = orderViewModel,
            )
        }

        composable<RunningOrderScreen> {
            RunningOrderScreen2()
        }
    }
}