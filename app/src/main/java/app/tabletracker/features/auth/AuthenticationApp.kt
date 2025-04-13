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
            ScanQrCodeScreen(
                authViewModel = it.accessSharedViewModel<AuthViewModel>(navController),
                onScanQrCode = {
                    navController.navigate(Screen.SyncRestaurantInfoScreen)
                }
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