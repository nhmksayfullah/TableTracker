package app.tabletracker.features.inventory.ui.screen.explorer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.tabletracker.R
import app.tabletracker.core.ui.SplitRatio
import app.tabletracker.core.ui.SplitScreen
import app.tabletracker.core.ui.component.CategoryComponent
import app.tabletracker.features.inventory.data.entity.Category
import app.tabletracker.features.inventory.data.entity.CategoryWithSubcategoriesAndMenuItems
import app.tabletracker.features.inventory.data.entity.MenuItem
import app.tabletracker.features.inventory.ui.EditMenuUiEvent
import app.tabletracker.features.inventory.ui.EditMenuViewModel
import app.tabletracker.features.inventory.ui.screen.category.AddNewCategoryDialog
import app.tabletracker.features.inventory.util.DatabaseOperation
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState

@Composable
fun InventoryExplorerScreen(
    viewModel: EditMenuViewModel,
    modifier: Modifier = Modifier,
    onCategoryClicked: (CategoryWithSubcategoriesAndMenuItems) -> Unit = {},
    onAddNewCategory: () -> Unit = {},
    onCategoryReordered: (List<Category>) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var canDrag by remember { mutableStateOf(false) }
    val lazyGridState = rememberLazyGridState()

    var updatedCategories by remember { mutableStateOf(emptyList<Category>()) }
    LaunchedEffect(uiState.explorer) {
        updatedCategories = uiState.explorer.map { it.category }.sortedBy { it.index }
    }

    val reorderableLazyGridState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
        updatedCategories = updatedCategories.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    }

    // Add drawer state and coroutine scope for handling drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Add state for selected category and database operation
    var selectedCategory by remember { mutableStateOf(Category(name = "")) }
    var databaseOperation by remember { mutableStateOf(DatabaseOperation.Read) }

    // Reset database operation when drawer is closed
    LaunchedEffect(drawerState.isClosed) {
        if (drawerState.isClosed) {
            databaseOperation = DatabaseOperation.Read
        }
    }

    SplitScreen(
        modifier = modifier,
        ratio = SplitRatio(leftWeight = 0f),
        rightContent = {
            Scaffold(
                floatingActionButton = {
                    Row {
                        FloatingActionButton(
                            onClick = {
                                if (canDrag) {
                                    canDrag = false
                                    onCategoryReordered(updatedCategories)
                                } else {
                                    canDrag = true
                                }
                            },
                            contentColor = MaterialTheme.colorScheme.primary,
                            containerColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            if (canDrag) {
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    contentDescription = "Reorder End"
                                )
                            } else {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.baseline_swap_calls_24),
                                    contentDescription = "Reorder Start"
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        ExtendedFloatingActionButton(
                            onClick = {
                                // Open drawer instead of navigating
                                selectedCategory = Category(name = "")
                                databaseOperation = DatabaseOperation.Add
                                scope.launch {
                                    drawerState.open()
                                }
                            },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Text("New Category")
                        }
                    }
                }
            ) { paddingValues ->
                LazyVerticalGrid(
                    state = lazyGridState,
                    columns = GridCells.Adaptive(120.dp),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    items(updatedCategories, key = { it.id }) { category ->
                        ReorderableItem(
                            reorderableLazyGridState,
                            key = category.id,
                            modifier = Modifier.padding(2.dp)
                        ) {
                            // Find the corresponding categoryWithItems for this category
                            val categoryWithItems = uiState.explorer.find { it.category.id == category.id }
                            if (categoryWithItems != null) {
                                CategoryComponent(
                                    text = category.name,
                                    modifier = Modifier,
                                    containerColor = category.color,
                                    onClick = { 
                                        // Handle click on category
                                        if (canDrag) {
                                            // If in drag mode, do nothing
                                        } else {
                                            // If not in drag mode, either open drawer for editing or navigate
                                            selectedCategory = category
                                            databaseOperation = DatabaseOperation.Edit
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        }
                                    }
                                )
                                if (canDrag) {
                                    IconButton(
                                        onClick = {},
                                        modifier = Modifier.draggableHandle()
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.baseline_drag_indicator_24),
                                            contentDescription = "Drag Icon"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        drawerState = drawerState,
        drawerContent = {
            AddNewCategoryDialog(
                category = selectedCategory,
                databaseOperation = databaseOperation,
                onValueChange = {
                    selectedCategory = it
                },
                onCreateClick = {
                    viewModel.onEvent(EditMenuUiEvent.UpsertCategory(selectedCategory))
                    scope.launch {
                        drawerState.close()
                    }
                },
                onDeleteClick = {
                    viewModel.onEvent(EditMenuUiEvent.DeleteCategory(selectedCategory))
                    scope.launch {
                        drawerState.close()
                    }
                },
                onUpdatedClick = {
                    viewModel.onEvent(EditMenuUiEvent.UpsertCategory(selectedCategory))
                    scope.launch {
                        drawerState.close()
                    }
                },
                onShowFoodsClick = {
                    scope.launch {
                        drawerState.close()
                        // Find the category with items and navigate to it
                        val categoryWithItems = uiState.explorer.find { it.category.id == selectedCategory.id }
                        if (categoryWithItems != null) {
                            onCategoryClicked(categoryWithItems)
                        }
                    }
                }
            )
        },
        leftContent = {}
    )
}
