package app.tabletracker.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.tabletracker.auth.ui.AuthViewModel
import app.tabletracker.auth.ui.screen.GetStartedScreen
import app.tabletracker.auth.ui.screen.RegisterLicenceScreen
import app.tabletracker.auth.ui.screen.RegisterRestaurantScreen
import app.tabletracker.auth.ui.screen.ScanQrCodeScreen
import app.tabletracker.auth.ui.screen.SyncRestaurantInfoScreen
import app.tabletracker.core.navigation.Screen
import app.tabletracker.util.accessSharedViewModel
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
                onScanQrCode = {
                    val (_, _) = it.split(":")
                }
            )
        }
        composable<Screen.SyncRestaurantInfoScreen> {

            SyncRestaurantInfoScreen(
                authViewModel = it.accessSharedViewModel<AuthViewModel>(navController),
            )
        }
    }
}