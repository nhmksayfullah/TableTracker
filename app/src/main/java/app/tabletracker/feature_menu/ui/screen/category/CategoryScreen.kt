package app.tabletracker.feature_menu.ui.screen.category

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
import kotlinx.coroutines.launch

@Composable
fun CategoryScreen(
    editMenuViewModel: EditMenuViewModel,
    modifier: Modifier = Modifier,
    onCategoryDoubleClicked: () -> Unit
) {
    val state by editMenuViewModel.uiState.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    SplitScreen(
        ratio = SplitRatio(leftWeight = 0f),
        rightContent = {
            ShowCategoriesRightSection(
                categories = state.menus.map { it.category },
                onCategoryClicked = {
                    editMenuViewModel.onEvent(EditMenuUiEvent.SetSelectedCategory(it))
                    scope.launch {
                        drawerState.open()
                    }
                },
                onCategoryDoubleClicked = {
                    editMenuViewModel.onEvent(EditMenuUiEvent.SetSelectedCategory(it))
                    onCategoryDoubleClicked()
                },
                onAddNewCategory = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                onCategoryReordered = {
                    editMenuViewModel.onEvent(EditMenuUiEvent.ReorderCategories(it))
                }
            )
        },
        drawerState = drawerState,
        drawerContent = {
            AddNewCategoryDialog(
                onCreateClick = {
                    editMenuViewModel.onEvent(EditMenuUiEvent.UpsertCategory(it))
                }
            )
        },
        leftContent = {}
    )
}