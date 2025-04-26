package app.tabletracker.features.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.tabletracker.features.auth.ui.AuthViewModel
import app.tabletracker.features.auth.ui.screen.GetStartedScreen
import app.tabletracker.features.auth.ui.screen.RegisterLicenceScreen
import app.tabletracker.features.auth.ui.screen.RegisterRestaurantScreen
import app.tabletracker.features.auth.ui.screen.ScanQrCodeScreen
import app.tabletracker.features.auth.ui.screen.SyncRestaurantInfoScreen
import app.tabletracker.navigation.Screen
import app.tabletracker.di.accessSharedViewModel
import kotlinx.serialization.Serializable

@Serializable
data object AuthenticationApp

fun NavGraphBuilder.authenticationNavGraph(
    navController: NavHostController,
    onRegistrationSuccessful: () -> Unit
) {
    navigation<AuthenticationApp>(
        startDestination = Screen.GetStartedScreen
    ) {
        composable<Screen.GetStartedScreen> {
            GetStartedScreen(
                onRegisterClick = {
                    navController.navigate(Screen.RegisterRestaurantScreen)
                },
                onLoginClick = {},
                onStartAsCompanionClick = {
                    navController.navigate(Screen.ScanQrCodeScreen)
                }
            )
        }
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
        composable<Screen.ScanQrCodeScreen> {
            // Check if we're coming from StartOrderScreen by looking at the previous backstack entry
            val isFromStartOrderScreen = navController.previousBackStackEntry?.destination?.route?.contains("StartOrderScreen") == true

            ScanQrCodeScreen(
                authViewModel = it.accessSharedViewModel<AuthViewModel>(navController),
                onScanQrCode = { data ->
                    if (isFromStartOrderScreen) {
                        // If we're coming from StartOrderScreen, just pop back to it
                        navController.popBackStack()
                    } else {
                        // Normal flow - go to SyncRestaurantInfoScreen
                        navController.navigate(Screen.SyncRestaurantInfoScreen)
                    }
                },
                isFromStartOrderScreen = isFromStartOrderScreen
            )
        }
        composable<Screen.SyncRestaurantInfoScreen> {

            SyncRestaurantInfoScreen(
                authViewModel = it.accessSharedViewModel<AuthViewModel>(navController),
                onGetStarted = onRegistrationSuccessful
            )
        }
    }
}
