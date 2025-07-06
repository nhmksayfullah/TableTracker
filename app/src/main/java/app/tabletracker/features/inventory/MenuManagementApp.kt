package app.tabletracker.features.inventory

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import app.tabletracker.navigation.Screen
import app.tabletracker.features.inventory.ui.EditMenuViewModel
import app.tabletracker.features.inventory.ui.EditMenuUiEvent
import app.tabletracker.features.inventory.ui.screen.category.CategoryScreen
import app.tabletracker.features.inventory.ui.screen.menuitem.MenuItemScreen
import app.tabletracker.di.accessSharedViewModel
import app.tabletracker.features.inventory.ui.screen.explorer.InventoryExplorerScreen
import app.tabletracker.features.inventory.ui.screen.explorer.ChildInventoryExplorerScreen
import app.tabletracker.features.inventory.data.entity.CategoryWithSubcategoriesAndMenuItems
import app.tabletracker.features.inventory.data.entity.MenuItem
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable

@Serializable
data object InventoryApp

fun NavGraphBuilder.inventoryNavGraph(
    navController: NavHostController,
) {

    navigation<InventoryApp>(
        startDestination = Screen.ExplorerScreen
    ) {


        composable<Screen.ExplorerScreen> {
            val editMenuViewModel = it.accessSharedViewModel<EditMenuViewModel>(navController)
            InventoryExplorerScreen(
                viewModel = editMenuViewModel,
                onCategoryClicked = { categoryWithItems ->
                    navController.navigate(Screen.ChildExplorerScreen(categoryWithItems.category.id))
                },
                onAddNewCategory = {
                    // No navigation needed, drawer is used directly in InventoryExplorerScreen
                },
                onCategoryReordered = { categories ->
                    editMenuViewModel.onEvent(EditMenuUiEvent.ReorderCategories(categories))
                }
            )
        }

        composable<Screen.ChildExplorerScreen> { backStackEntry ->
            val editMenuViewModel = backStackEntry.accessSharedViewModel<EditMenuViewModel>(navController)
            val categoryId = backStackEntry.toRoute<Screen.ChildExplorerScreen>().categoryId
            val uiState by editMenuViewModel.uiState.collectAsStateWithLifecycle()

            ChildInventoryExplorerScreen(
                viewModel = editMenuViewModel,
                categoryId = categoryId,
                onCategoryClicked = { categoryWithItems: CategoryWithSubcategoriesAndMenuItems ->
                    navController.navigate(Screen.ChildExplorerScreen(categoryWithItems.category.id))
                },
                onBackClick = {
                    navController.navigateUp()
                },
                onAddMenuItem = { parentCategoryId ->
                    // No navigation needed, drawer is used directly in ChildInventoryExplorerScreen
                },
                onAddSubcategory = { parentCategoryId ->
                    // No navigation needed, drawer is used directly in ChildInventoryExplorerScreen
                },
                onSubcategoriesReordered = { categories ->
                    editMenuViewModel.onEvent(EditMenuUiEvent.ReorderCategories(categories))
                },
                onMenuItemsReordered = { menuItems ->
                    editMenuViewModel.onEvent(EditMenuUiEvent.ReorderMenuItems(menuItems))
                }
            )
        }

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
                    // This navigation is deprecated, but kept for backward compatibility
                    // Use ChildInventoryExplorerScreen with drawer instead
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
