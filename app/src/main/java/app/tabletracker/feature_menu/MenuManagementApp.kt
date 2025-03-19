package app.tabletracker.feature_menu

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.tabletracker.core.navigation.Screen
import app.tabletracker.feature_menu.ui.EditMenuViewModel
import app.tabletracker.feature_menu.ui.screen.category.CategoryScreen
import app.tabletracker.feature_menu.ui.screen.menuitem.MenuItemScreen
import app.tabletracker.util.accessSharedViewModel
import kotlinx.serialization.Serializable

@Serializable
data object InventoryApp

fun NavGraphBuilder.inventoryNavGraph(
    navController: NavHostController,
) {

    navigation<InventoryApp>(
        startDestination = Screen.CategoryScreen
    ) {

        composable<Screen.CategoryScreen>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        delayMillis = 250,
                        durationMillis = 1000
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        durationMillis = 1000
                    )
                )
            }
        ) {
            val editMenuViewModel = it.accessSharedViewModel<EditMenuViewModel>(navController)
            CategoryScreen(
                editMenuViewModel = editMenuViewModel,
                onNavigateToMenuItems = {
                    navController.navigate(Screen.MenuItemScreen)
                }
            )
        }

        composable<Screen.MenuItemScreen>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        delayMillis = 250,
                        durationMillis = 1000
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        durationMillis = 1000
                    )
                )
            }
        ) {
            val editMenuViewModel = it.accessSharedViewModel<EditMenuViewModel>(navController)
            MenuItemScreen(
                editMenuViewModel = editMenuViewModel
            )
        }
    }

}