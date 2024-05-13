package app.tabletracker.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.tabletracker.app.ui.AppUiEvent
import app.tabletracker.core.navigation.Applications
import app.tabletracker.core.navigation.Screen
import app.tabletracker.settings.ui.SettingsViewModel
import app.tabletracker.settings.ui.screen.SettingsScreen
import app.tabletracker.util.accessSharedViewModel

fun NavGraphBuilder.settingsApp(
    navController: NavHostController,
    onAppUiEvent: (AppUiEvent) -> Unit
) {
    navigation(
        route = Applications.SettingsApp.route,
        startDestination = Screen.SettingsScreen.route
    ) {
        composable(Screen.SettingsScreen.route) {
            val viewModel = it.accessSharedViewModel<SettingsViewModel>(navController = navController)
            SettingsScreen(
                settingsViewModel = viewModel
            ) {
                onAppUiEvent(AppUiEvent.ChangeScreen(Screen.StartOrderScreen))
                navController.popBackStack()
            }
        }
    }
}