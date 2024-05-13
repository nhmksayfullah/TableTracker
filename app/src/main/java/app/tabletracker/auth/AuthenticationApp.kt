package app.tabletracker.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.tabletracker.app.ui.AppUiEvent
import app.tabletracker.core.navigation.Screen
import app.tabletracker.auth.ui.AuthViewModel
import app.tabletracker.auth.ui.screen.RegisterLicenceScreen
import app.tabletracker.auth.ui.screen.RegisterRestaurantScreen
import app.tabletracker.core.navigation.Applications
import app.tabletracker.util.accessSharedViewModel

fun NavGraphBuilder.authenticationApp(
    navController: NavHostController,
    onAppUiEvent: (AppUiEvent) -> Unit
) {
    navigation(
        route = Applications.AuthenticationApp.route,
        startDestination = Screen.RegisterRestaurantScreen.route
    ) {
        composable(Screen.RegisterRestaurantScreen.route) {
            RegisterRestaurantScreen(
                authViewModel = it.accessSharedViewModel<AuthViewModel>(navController),
                onRegisterClick = {
                    navController.navigate(Screen.RegisterLicenceScreen.route)

                }
            )
        }
        composable(Screen.RegisterLicenceScreen.route) {
            RegisterLicenceScreen(
                authViewModel = it.accessSharedViewModel<AuthViewModel>(navController),
                onRegistrationSuccessful = {
                    onAppUiEvent(AppUiEvent.ChangeApplication(Applications.LoadingApp))
                    onAppUiEvent(AppUiEvent.ChangeScreen(Screen.LoadingScreen))
                    navController.navigate(Applications.LoadingApp.route)
                }
            )
        }
    }
}