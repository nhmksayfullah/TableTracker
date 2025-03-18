package app.tabletracker.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.tabletracker.core.navigation.Screen
import app.tabletracker.settings.ui.SettingsViewModel
import app.tabletracker.settings.ui.screen.SettingsScreen
import app.tabletracker.util.accessSharedViewModel
import kotlinx.serialization.Serializable

@Serializable
data object SettingsApp

fun NavGraphBuilder.settingsNavGraph(
    navController: NavHostController,
) {
    navigation<SettingsApp>(
        startDestination = Screen.SettingsScreen
    ) {
        composable<Screen.SettingsScreen> {
            val viewModel = it.accessSharedViewModel<SettingsViewModel>(navController = navController)
            SettingsScreen(
                settingsViewModel = viewModel
            ) {
                navController.popBackStack()
            }
        }
    }
}