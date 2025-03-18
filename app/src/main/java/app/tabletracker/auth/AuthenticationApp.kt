package app.tabletracker.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.tabletracker.core.navigation.Screen
import app.tabletracker.auth.ui.AuthViewModel
import app.tabletracker.auth.ui.screen.RegisterLicenceScreen
import app.tabletracker.auth.ui.screen.RegisterRestaurantScreen
import app.tabletracker.feature_order.OrderManagementApp
import app.tabletracker.util.accessSharedViewModel
import kotlinx.serialization.Serializable

@Serializable
data object AuthenticationApp

fun NavGraphBuilder.authenticationNavGraph(
    navController: NavHostController,
    onRegistrationSuccessful: () -> Unit
) {
    navigation<AuthenticationApp>(
        startDestination = Screen.RegisterRestaurantScreen
    ) {
        composable<Screen.RegisterRestaurantScreen> {
            RegisterRestaurantScreen(
                authViewModel = it.accessSharedViewModel<AuthViewModel>(navController),
                onRegisterClick = {
                    navController.navigate(Screen.RegisterLicenceScreen)

                }
            )
        }
        composable<Screen.RegisterLicenceScreen> {
            RegisterLicenceScreen(
                authViewModel = it.accessSharedViewModel<AuthViewModel>(navController),
                onRegistrationSuccessful = onRegistrationSuccessful
            )
        }
    }
}