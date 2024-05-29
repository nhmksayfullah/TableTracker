package app.tabletracker.feature_menu.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.tabletracker.app.ui.AppUiEvent
import app.tabletracker.app.ui.AppUiState
import app.tabletracker.core.ui.SplitScreen
import app.tabletracker.feature_menu.ui.EditMenuUiEvent
import app.tabletracker.feature_menu.ui.EditMenuViewModel
import app.tabletracker.feature_menu.ui.section.EditCategoryLeftSection
import app.tabletracker.feature_menu.ui.section.EditMenuItemLeftSection
import app.tabletracker.feature_menu.ui.section.EditMenuRightSection

@Composable
fun EditMenuScreen(
    appUiState: AppUiState,
    onAppUiEvent: (AppUiEvent) -> Unit,
    editMenuViewModel: EditMenuViewModel,
) {
    BackHandler(
        true
    ) {

    }
    val uiState by editMenuViewModel.uiState.collectAsState()
    var leftSectionState: LeftSectionState by rememberSaveable {
        mutableStateOf(LeftSectionState.AddNewCategory)
    }

    SplitScreen(
        leftContent = {
            when (leftSectionState) {
                LeftSectionState.AddNewCategory -> {
                    EditCategoryLeftSection(
                        update = false,
                        category = uiState.selectedCategory,
                        onCategoryChange = {
                            editMenuViewModel.onEvent(EditMenuUiEvent.ChangeCategory(it))
                        },
                        onUpsertClick = {
                            editMenuViewModel.onEvent(EditMenuUiEvent.UpsertCategory(it))
                        },
                        onDeleteClick = {

                        }
                    )
                }

                LeftSectionState.EditCategory -> {
                    EditCategoryLeftSection(
                        update = true,
                        category = uiState.selectedCategory,
                        onCategoryChange = {
                            editMenuViewModel.onEvent(EditMenuUiEvent.ChangeCategory(it))
                        },
                        onUpsertClick = {
                            editMenuViewModel.onEvent(EditMenuUiEvent.UpsertCategory(it))
                        },
                        onDeleteClick = {
                            editMenuViewModel.onEvent(EditMenuUiEvent.DeleteCategory(it))
                        }
                    )
                }

                LeftSectionState.AddNewMenuItem -> {
                    EditMenuItemLeftSection(
                        update = false,
                        menus = uiState.menus,
                        menuItem = uiState.selectedMenuItem,
                        onMenuItemDetailsChange = {
                            editMenuViewModel.onEvent(EditMenuUiEvent.ChangeDetailsOfMenuItem(it))
                        },
                        onMenuItemPricesChange = {
                            editMenuViewModel.onEvent(EditMenuUiEvent.ChangePricesOfMenuItem(it))

                        },
                        onUpsertClick = {
                            editMenuViewModel.onEvent(EditMenuUiEvent.UpsertMenuItem(it))

                        },
                        onDeleteClick = {

                        }
                    )
                }

                LeftSectionState.EditMenuItem -> {
                    EditMenuItemLeftSection(
                        update = true,
                        menus = uiState.menus,
                        menuItem = uiState.selectedMenuItem,
                        onMenuItemDetailsChange = {
                            editMenuViewModel.onEvent(EditMenuUiEvent.ChangeDetailsOfMenuItem(it))
                        },
                        onMenuItemPricesChange = {
                            editMenuViewModel.onEvent(EditMenuUiEvent.ChangePricesOfMenuItem(it))

                        },
                        onUpsertClick = {
                            editMenuViewModel.onEvent(EditMenuUiEvent.UpsertMenuItem(it))

                        },
                        onDeleteClick = {
                            editMenuViewModel.onEvent(EditMenuUiEvent.DeleteMenuItem(it))
                        }
                    )
                }
            }
        },
        rightContent = {
            EditMenuRightSection(
                menus = uiState.menus,
                onCategoryClicked = {
                    editMenuViewModel.onEvent(EditMenuUiEvent.ChangeCategory(it))
                    leftSectionState = LeftSectionState.EditCategory
                },
                onMenuItemClicked = {
                    editMenuViewModel.onEvent(EditMenuUiEvent.ChangeDetailsOfMenuItem(it))
                    leftSectionState = LeftSectionState.EditMenuItem
                },
                onAddNewCategory = {
                    editMenuViewModel.onEvent(EditMenuUiEvent.AddNewCategory)
                    leftSectionState = LeftSectionState.AddNewCategory
                },
                onAddNewMenuItem = {
                    editMenuViewModel.onEvent(EditMenuUiEvent.AddNewMenuItem)
                    leftSectionState = LeftSectionState.AddNewMenuItem

                }
            )
        }
    )

}

enum class LeftSectionState {
    AddNewCategory, EditCategory,
    AddNewMenuItem, EditMenuItem
}