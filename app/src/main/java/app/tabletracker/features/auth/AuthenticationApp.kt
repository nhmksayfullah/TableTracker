package app.tabletracker.features.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import app.tabletracker.features.auth.ui.AuthViewModel
import app.tabletracker.features.auth.ui.screen.GetStartedScreen
import app.tabletracker.features.auth.ui.screen.RegisterLicenceScreen
import app.tabletracker.features.auth.ui.screen.RegisterRestaurantScreen
import app.tabletracker.features.auth.ui.screen.ScanQrCodeScreen
import app.tabletracker.features.auth.ui.screen.SyncRestaurantInfoScreen
import app.tabletracker.navigation.Screen
import app.tabletracker.di.accessSharedViewModel
import app.tabletracker.features.auth.data.model.DeviceType
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

            val authViewModel = it.accessSharedViewModel<AuthViewModel>(navController)
            ScanQrCodeScreen(
                authViewModel = authViewModel,
                onScanQrCode = { data ->
                    navController.navigate(Screen.SyncRestaurantInfoScreen(data))
                },
//                isFromStartOrderScreen = false
            )
        }
        composable<Screen.SyncRestaurantInfoScreen> {
            val authViewModel = it.accessSharedViewModel<AuthViewModel>(navController)
            val serverAddress = it.toRoute<Screen.SyncRestaurantInfoScreen>().serverAddress
            val ipAddress = serverAddress.split(":")[0]
            val port = serverAddress.split(":")[1].toInt()
            authViewModel.connectToServer(ipAddress, port)
            SyncRestaurantInfoScreen(
                authViewModel = authViewModel,
                onGetStarted = {
                    authViewModel.updateDeviceType(DeviceType.Companion)
                    onRegistrationSuccessful()
                }
            )
        }
    }
}
