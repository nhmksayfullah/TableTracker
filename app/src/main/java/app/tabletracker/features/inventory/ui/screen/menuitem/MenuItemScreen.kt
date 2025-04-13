package app.tabletracker.features.inventory.ui.screen.menuitem

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
import app.tabletracker.features.inventory.data.entity.MenuItem
import app.tabletracker.features.inventory.ui.EditMenuUiEvent
import app.tabletracker.features.inventory.ui.EditMenuViewModel
import app.tabletracker.features.inventory.util.DatabaseOperation
import kotlinx.coroutines.launch

@Composable
fun MenuItemScreen(
    editMenuViewModel: EditMenuViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by editMenuViewModel.uiState.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var databaseOperation by remember {
        mutableStateOf(DatabaseOperation.Read)
    }
    LaunchedEffect(Unit) {
        uiState.selectedCategory?.let {
            editMenuViewModel.onEvent(
                EditMenuUiEvent.SetSelectedMenuItem(
                    MenuItem.empty(it.id)
                )
            )
        }
    }

    SplitScreen(
        modifier = modifier,
        ratio = SplitRatio(leftWeight = 0f),
        rightContent = {
            ShowMenuItemsRightSection(
                menuItems = uiState.menus.find { it.category == uiState.selectedCategory }?.menuItems
                    ?: emptyList(),
                onMenuItemClicked = {
                    editMenuViewModel.onEvent(EditMenuUiEvent.SetSelectedMenuItem(it))
                    databaseOperation = DatabaseOperation.Edit

                    scope.launch {
                        drawerState.open()
                    }
                },
                onAddNewMenuItem = {
                    uiState.selectedCategory?.let {
                        databaseOperation = DatabaseOperation.Add
                        editMenuViewModel.onEvent(
                            EditMenuUiEvent.SetSelectedMenuItem(
                                MenuItem.empty(categoryId = it.id)
                            )
                        )
                        scope.launch {
                            drawerState.open()
                        }
                    }

                },
                onMenuItemsReordered = {
                    editMenuViewModel.onEvent(EditMenuUiEvent.ReorderMenuItems(it))
                }
            )
        },
        drawerState = drawerState,
        drawerContent = {
            uiState.selectedCategory?.let { category ->
                uiState.selectedMenuItem?.let { menuItem ->
                    AddNewMenuItemDialog(
                        menuItem = menuItem,
                        databaseOperation = databaseOperation,
                        onValueChange = {
                            editMenuViewModel.onEvent(EditMenuUiEvent.UpdateSelectedMenuItem(it))
                        },
                        onCreateClick = { newPrices ->
                            editMenuViewModel.onEvent(
                                EditMenuUiEvent.UpsertMenuItem(
                                    menuItem,
                                    newPrices
                                )
                            )
                            scope.launch {
                                drawerState.close()
                            }
                            databaseOperation = DatabaseOperation.Read
                        },
                        onDeleteClick = {
                            editMenuViewModel.onEvent(EditMenuUiEvent.DeleteMenuItem(menuItem))
                            scope.launch {
                                drawerState.close()
                            }
                            databaseOperation = DatabaseOperation.Read
                        },
                        onUpdatedClick = { newPrices ->
                            editMenuViewModel.onEvent(
                                EditMenuUiEvent.UpsertMenuItem(
                                    menuItem,
                                    newPrices
                                )
                            )
                            scope.launch {
                                drawerState.close()
                            }
                            databaseOperation = DatabaseOperation.Read
                        }
                    )
                }
            }
        },
        leftContent = {}
    )
}