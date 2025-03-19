package app.tabletracker.feature_menu.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.tabletracker.core.ui.SplitScreen
import app.tabletracker.feature_menu.ui.EditMenuUiEvent
import app.tabletracker.feature_menu.ui.EditMenuViewModel
import app.tabletracker.feature_menu.ui.section.AddEditCategoryLeftSection
import app.tabletracker.feature_menu.ui.section.AddEditMenuItemLeftSection
import app.tabletracker.feature_menu.ui.section.ShowCategoriesRightSection
import app.tabletracker.feature_menu.ui.section.ShowMenuItemsRightSection

@Composable
fun InventoryScreen(
    editMenuViewModel: EditMenuViewModel,
    modifier: Modifier = Modifier
) {


    val state by editMenuViewModel.uiState.collectAsState()
    var leftState: LeftState by remember {
        mutableStateOf(LeftState.Nothing)
    }
    var rightState: RightState by remember {
        mutableStateOf(RightState.Category)
    }

    BackHandler {
        if (leftState == LeftState.EditCategory) {
            leftState = LeftState.Nothing
        } else if (leftState == LeftState.EditMenuItem) {
            leftState = LeftState.Nothing
        }
        if (rightState == RightState.MenuItem) {
            rightState = RightState.Category
        }
    }


    SplitScreen(
        leftContent = {
            when (leftState) {
                LeftState.EditCategory -> {
                    AddEditCategoryLeftSection(
                        category = state.selectedCategory,
                        modifier = Modifier,
                        onEditMenu = editMenuViewModel::onEvent
                    )
                }

                LeftState.EditMenuItem -> {
                    AddEditMenuItemLeftSection(
                        menuItem = state.selectedMenuItem,
                        modifier = Modifier,
                        onEditMenu = editMenuViewModel::onEvent
                    )
                }

                LeftState.Nothing -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Select an item")
                    }
                }
            }
        },
        rightContent = {
            when (rightState) {
                RightState.Category -> {
                    ShowCategoriesRightSection(
                        categories = state.menus.map { it.category },
                        onCategoryClicked = {
                            leftState = LeftState.EditCategory
                            editMenuViewModel.onEvent(EditMenuUiEvent.ChangeCategory(it))
                        },
                        onCategoryDoubleClicked = {
                            rightState = RightState.MenuItem
                            leftState = LeftState.Nothing
                            editMenuViewModel.onEvent(EditMenuUiEvent.ChangeCategory(it))
                        },
                        onAddNewCategory = {
                            leftState = LeftState.EditCategory
                            editMenuViewModel.onEvent(EditMenuUiEvent.AddNewCategory)
                        },
                        onCategoryReordered = {
                            editMenuViewModel.onEvent(EditMenuUiEvent.ReorderCategories(it))
                        }
                    )
                }

                RightState.MenuItem -> {
                    ShowMenuItemsRightSection(
                        menuItems = state.menus.find { it.category == state.selectedCategory }?.menuItems
                            ?: emptyList(),
                        onMenuItemClicked = {
                            editMenuViewModel.onEvent(EditMenuUiEvent.ChangeDetailsOfMenuItem(it))
                            leftState = LeftState.EditMenuItem
                        },
                        onAddNewMenuItem = {
                            leftState = LeftState.EditMenuItem
                            editMenuViewModel.onEvent(EditMenuUiEvent.AddNewMenuItem)
                        },
                        onMenuItemsReordered = {
                            editMenuViewModel.onEvent(EditMenuUiEvent.ReorderMenuItems(it))
                        }
                    )
                }
            }
        }
    )
}

enum class LeftState {
    EditCategory, EditMenuItem, Nothing
}

enum class RightState {
    Category, MenuItem
}