package app.tabletracker.feature_menu.ui.screen.category

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.tabletracker.core.ui.SplitRatio
import app.tabletracker.core.ui.SplitScreen
import app.tabletracker.feature_menu.data.entity.Category
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_menu.ui.EditMenuUiEvent
import app.tabletracker.feature_menu.ui.EditMenuViewModel
import app.tabletracker.feature_menu.util.DatabaseOperation
import kotlinx.coroutines.launch

@Composable
fun CategoryScreen(
    editMenuViewModel: EditMenuViewModel,
    modifier: Modifier = Modifier,
    onNavigateToMenuItems: () -> Unit,
) {
    val state by editMenuViewModel.uiState.collectAsStateWithLifecycle()
    var databaseOperation by remember {
        mutableStateOf(DatabaseOperation.Read)
    }
    var selectedCategory: Category by remember {
        mutableStateOf(
            Category(name = "")
        )
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(drawerState.isClosed) {
        if (drawerState.isClosed) {
            databaseOperation = DatabaseOperation.Read
        }
    }

    SplitScreen(
        modifier = modifier,
        ratio = SplitRatio(leftWeight = 0f),
        rightContent = {
            ShowCategoriesRightSection(
                categories = state.menus.map { it.category },
                onCategoryClicked = {
                    selectedCategory = it
                    editMenuViewModel.onEvent(EditMenuUiEvent.SetSelectedCategory(it))
                    databaseOperation = DatabaseOperation.Edit
                    scope.launch {
                        drawerState.open()
                    }
                },
                onAddNewCategory = {
                    scope.launch {
                        selectedCategory = Category(name = "")
                        databaseOperation = DatabaseOperation.Add
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
            selectedCategory.let {
                AddNewCategoryDialog(
                    category = it,
                    databaseOperation = databaseOperation,
                    onValueChange = {
                        selectedCategory = it
                    },
                    onCreateClick = {
                        selectedCategory = Category(name = "")
                        editMenuViewModel.onEvent(EditMenuUiEvent.UpsertCategory(it))
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    onDeleteClick = {
                        selectedCategory = Category(name = "")
                        editMenuViewModel.onEvent(EditMenuUiEvent.DeleteCategory(it))
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    onUpdatedClick = {
                        selectedCategory = Category(name = "")
                        editMenuViewModel.onEvent(EditMenuUiEvent.UpsertCategory(it))
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    onShowFoodsClick = {
                        scope.launch {
                            drawerState.close()
                            editMenuViewModel.onEvent(EditMenuUiEvent.SetSelectedCategory(it))
                            editMenuViewModel.onEvent(
                                EditMenuUiEvent.SetSelectedMenuItem(
                                    MenuItem(
                                        name = "",
                                        abbreviation = "",
                                        categoryId = it.id,
                                        prices = mapOf(),
                                    )
                                )
                            )
                            onNavigateToMenuItems()
                        }
                    }
                )
            }
        },
        leftContent = {}
    )
}