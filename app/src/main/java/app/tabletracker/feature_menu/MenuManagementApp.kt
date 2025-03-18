package app.tabletracker.feature_menu

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.tabletracker.core.navigation.Screen
import app.tabletracker.feature_menu.ui.EditMenuViewModel
import app.tabletracker.feature_menu.ui.screen.InventoryScreen
import app.tabletracker.util.accessSharedViewModel
import kotlinx.serialization.Serializable

@Serializable
data object InventoryApp

fun NavGraphBuilder.inventoryNavGraph(
    navController: NavHostController,
) {

    navigation<InventoryApp>(
        startDestination = Screen.InventoryScreen
    ) {

        composable<Screen.InventoryScreen> {
            val editMenuViewModel = it.accessSharedViewModel<EditMenuViewModel>(navController)
            InventoryScreen(
                editMenuViewModel = editMenuViewModel
            )
        }
    }

}