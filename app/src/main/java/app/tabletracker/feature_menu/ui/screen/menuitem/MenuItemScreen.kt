package app.tabletracker.feature_menu.ui.screen.menuitem

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.tabletracker.core.ui.SplitRatio
import app.tabletracker.core.ui.SplitScreen
import app.tabletracker.feature_menu.ui.EditMenuUiEvent
import app.tabletracker.feature_menu.ui.EditMenuViewModel
import app.tabletracker.feature_menu.ui.screen.category.ShowMenuItemsRightSection
import kotlinx.coroutines.launch

@Composable
fun MenuItemScreen(
    editMenuViewModel: EditMenuViewModel,
    modifier: Modifier = Modifier,
) {
    val state by editMenuViewModel.uiState.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    SplitScreen(
        ratio = SplitRatio(leftWeight = 0f),
        rightContent = {
            ShowMenuItemsRightSection(
                menuItems = state.menus.find { it.category == state.selectedCategory }?.menuItems
                    ?: emptyList(),
                onMenuItemClicked = {
                    editMenuViewModel.onEvent(EditMenuUiEvent.SetSelectedMenuItem(it))
                    scope.launch {
                        drawerState.open()
                    }
                },
                onAddNewMenuItem = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                onMenuItemsReordered = {
                    editMenuViewModel.onEvent(EditMenuUiEvent.ReorderMenuItems(it))
                }
            )
        },
        drawerState = drawerState,
        drawerContent = {
            state.selectedCategory?.let {
                AddNewMenuItemDialog(
                    selectedCategory = it,
                    onCreateClick = {

                    }
                )
            }
        },
        leftContent = {}
    )
}