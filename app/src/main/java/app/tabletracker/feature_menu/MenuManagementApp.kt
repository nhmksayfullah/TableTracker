package app.tabletracker.feature_menu

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.tabletracker.app.ui.AppUiEvent
import app.tabletracker.app.ui.AppUiState
import app.tabletracker.core.navigation.Screen
import app.tabletracker.core.navigation.Applications
import app.tabletracker.feature_menu.ui.EditMenuViewModel
import app.tabletracker.feature_menu.ui.screen.EditMenuScreen
import app.tabletracker.feature_menu.ui.screen.InventoryScreen
import app.tabletracker.util.accessSharedViewModel


fun NavGraphBuilder.menuManagementApp(
    navController: NavHostController,
) {

    navigation(
        route = Applications.MenuManagementApp.route,
        startDestination = Screen.EditMenuScreen.route
    ) {

        composable(Screen.EditMenuScreen.route) {
            val editMenuViewModel = it.accessSharedViewModel<EditMenuViewModel>(navController)
            InventoryScreen(
                editMenuViewModel = editMenuViewModel
            )
        }
    }

}