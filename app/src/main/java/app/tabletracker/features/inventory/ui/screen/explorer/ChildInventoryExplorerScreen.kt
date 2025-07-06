package app.tabletracker.features.inventory.ui.screen.explorer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.tabletracker.R
import app.tabletracker.core.ui.SplitRatio
import app.tabletracker.core.ui.SplitScreen
import app.tabletracker.core.ui.component.CategoryComponent
import app.tabletracker.core.ui.component.MenuItemComponent
import app.tabletracker.features.inventory.data.entity.Category
import app.tabletracker.features.inventory.data.entity.CategoryWithSubcategoriesAndMenuItems
import app.tabletracker.features.inventory.data.entity.MenuItem
import app.tabletracker.features.inventory.ui.EditMenuUiEvent
import app.tabletracker.features.inventory.ui.EditMenuViewModel
import app.tabletracker.features.inventory.ui.screen.category.AddNewCategoryDialog
import app.tabletracker.features.inventory.ui.screen.menuitem.AddNewMenuItemDialog
import app.tabletracker.features.inventory.util.DatabaseOperation
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState

/**
 * Builds a list of categories representing the breadcrumb trail from root to the current category
 */
private fun buildBreadcrumbTrail(
    categoryId: Int,
    allCategories: List<CategoryWithSubcategoriesAndMenuItems>
): List<Category> {
    val result = mutableListOf<Category>()

    // Find the current category
    val currentCategoryWithItems = allCategories.find { it.category.id == categoryId }
    if (currentCategoryWithItems != null) {
        // Add the current category to the result
        result.add(currentCategoryWithItems.category)

        // Recursively find and add parent categories
        var parentId = currentCategoryWithItems.category.parentCategoryId
        while (parentId != null) {
            val parentCategory = allCategories.find { it.category.id == parentId }
            if (parentCategory != null) {
                // Add parent to the beginning of the list
                result.add(0, parentCategory.category)
                parentId = parentCategory.category.parentCategoryId
            } else {
                break
            }
        }
    }

    return result
}

@Composable
fun BreadcrumbTrail(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEachIndexed { index, category ->
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (index == categories.size - 1) FontWeight.Bold else FontWeight.Normal,
                color = if (index == categories.size - 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.clickable { onCategoryClick(category) },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (index < categories.size - 1) {
                Text(
                    text = " > ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ChildInventoryExplorerScreen(
    viewModel: EditMenuViewModel,
    categoryId: Int,
    modifier: Modifier = Modifier,
    onCategoryClicked: (CategoryWithSubcategoriesAndMenuItems) -> Unit = {},
    onBackClick: () -> Unit = {},
    onAddMenuItem: (Int) -> Unit = {},
    onAddSubcategory: (Int) -> Unit = {},
    onSubcategoriesReordered: (List<Category>) -> Unit = {},
    onMenuItemsReordered: (List<MenuItem>) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Find the current category from the allCategories list
    val currentCategory = uiState.allCategories.find { it.category.id == categoryId }

    // Build the breadcrumb trail
    val breadcrumbCategories = remember<List<Category>>(currentCategory, uiState.allCategories) {
        buildBreadcrumbTrail(categoryId, uiState.allCategories)
    }

    var canDragSubcategories by remember { mutableStateOf(false) }
    var canDragMenuItems by remember { mutableStateOf(false) }

    val subcategoriesGridState = rememberLazyGridState()
    val menuItemsGridState = rememberLazyGridState()

    var updatedSubcategories by remember { mutableStateOf(emptyList<Category>()) }
    var updatedMenuItems by remember { mutableStateOf(emptyList<MenuItem>()) }

    LaunchedEffect(currentCategory) {
        currentCategory?.let {
            updatedSubcategories = it.subcategories.sortedBy { cat -> cat.index }
            updatedMenuItems = it.menuItems.sortedBy { item -> item.index }
        }
    }

    val reorderableSubcategoriesGridState = rememberReorderableLazyGridState(subcategoriesGridState) { from, to ->
        updatedSubcategories = updatedSubcategories.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    }

    val reorderableMenuItemsGridState = rememberReorderableLazyGridState(menuItemsGridState) { from, to ->
        updatedMenuItems = updatedMenuItems.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    }

    // Add drawer states and coroutine scope for handling drawers
    val categoryDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val menuItemDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Add state for selected category, menu item, and database operations
    var selectedSubcategory by remember { mutableStateOf(Category(name = "")) }
    var selectedMenuItem by remember { mutableStateOf(MenuItem.empty(categoryId)) }
    var categoryDatabaseOperation by remember { mutableStateOf(DatabaseOperation.Read) }
    var menuItemDatabaseOperation by remember { mutableStateOf(DatabaseOperation.Read) }

    // Reset database operations when drawers are closed
    LaunchedEffect(categoryDrawerState.isClosed) {
        if (categoryDrawerState.isClosed) {
            categoryDatabaseOperation = DatabaseOperation.Read
        }
    }

    LaunchedEffect(menuItemDrawerState.isClosed) {
        if (menuItemDrawerState.isClosed) {
            menuItemDatabaseOperation = DatabaseOperation.Read
        }
    }

    // Use SplitScreen for category drawer
    SplitScreen(
        modifier = modifier,
        ratio = SplitRatio(leftWeight = 0f),
        drawerState = categoryDrawerState,
        drawerContent = {
            AddNewCategoryDialog(
                category = selectedSubcategory,
                databaseOperation = categoryDatabaseOperation,
                onValueChange = {
                    selectedSubcategory = it
                },
                onCreateClick = {
                    // Set parent category ID for the new subcategory
                    val newCategory = selectedSubcategory.copy(parentCategoryId = categoryId)
                    viewModel.onEvent(EditMenuUiEvent.UpsertCategory(newCategory))
                    scope.launch {
                        categoryDrawerState.close()
                    }
                },
                onDeleteClick = {
                    viewModel.onEvent(EditMenuUiEvent.DeleteCategory(selectedSubcategory))
                    scope.launch {
                        categoryDrawerState.close()
                    }
                },
                onUpdatedClick = {
                    viewModel.onEvent(EditMenuUiEvent.UpsertCategory(selectedSubcategory))
                    scope.launch {
                        categoryDrawerState.close()
                    }
                },
                onShowFoodsClick = {
                    scope.launch {
                        categoryDrawerState.close()
                        // Find the subcategory with items and navigate to it
                        val subcategoryWithItems = uiState.allCategories.find { it.category.id == selectedSubcategory.id }
                        if (subcategoryWithItems != null) {
                            onCategoryClicked(subcategoryWithItems)
                        }
                    }
                }
            )
        },
        // Use another SplitScreen for menu item drawer
        rightContent = {
            SplitScreen(
                ratio = SplitRatio(leftWeight = 0f),
                drawerState = menuItemDrawerState,
                drawerContent = {
                    AddNewMenuItemDialog(
                        menuItem = selectedMenuItem,
                        databaseOperation = menuItemDatabaseOperation,
                        onValueChange = {
                            selectedMenuItem = it
                        },
                        onCreateClick = { newPrices ->
                            viewModel.onEvent(EditMenuUiEvent.UpsertMenuItem(selectedMenuItem, newPrices))
                            scope.launch {
                                menuItemDrawerState.close()
                            }
                        },
                        onDeleteClick = {
                            viewModel.onEvent(EditMenuUiEvent.DeleteMenuItem(selectedMenuItem))
                            scope.launch {
                                menuItemDrawerState.close()
                            }
                        },
                        onUpdatedClick = { newPrices ->
                            viewModel.onEvent(EditMenuUiEvent.UpsertMenuItem(selectedMenuItem, newPrices))
                            scope.launch {
                                menuItemDrawerState.close()
                            }
                        }
                    )
                },
                rightContent = {
                    Scaffold(
                        floatingActionButton = {
                            Row {
                                // Button for adding subcategory
                                if (currentCategory != null) {
                                    ExtendedFloatingActionButton(
                                        onClick = { 
                                            // Open category drawer instead of navigating
                                            selectedSubcategory = Category(name = "", parentCategoryId = categoryId)
                                            categoryDatabaseOperation = DatabaseOperation.Add
                                            scope.launch {
                                                categoryDrawerState.open()
                                            }
                                        },
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ) {
                                        Text("Add Subcategory")
                                    }

                                    Spacer(modifier = Modifier.width(4.dp))

                                    // Button for adding menu item
                                    ExtendedFloatingActionButton(
                                        onClick = { 
                                            // Open menu item drawer instead of navigating
                                            selectedMenuItem = MenuItem.empty(categoryId)
                                            menuItemDatabaseOperation = DatabaseOperation.Add
                                            scope.launch {
                                                menuItemDrawerState.open()
                                            }
                                        },
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ) {
                                        Text("Add Menu Item")
                                    }

                                    Spacer(modifier = Modifier.width(4.dp))

                                    // Button for reordering
                                    FloatingActionButton(
                                        onClick = {
                                            if (canDragSubcategories || canDragMenuItems) {
                                                if (canDragSubcategories) {
                                                    canDragSubcategories = false
                                                    onSubcategoriesReordered(updatedSubcategories)
                                                }
                                                if (canDragMenuItems) {
                                                    canDragMenuItems = false
                                                    onMenuItemsReordered(updatedMenuItems)
                                                }
                                            } else {
                                                canDragSubcategories = true
                                                canDragMenuItems = true
                                            }
                                        },
                                        contentColor = MaterialTheme.colorScheme.primary,
                                        containerColor = MaterialTheme.colorScheme.onPrimary
                                    ) {
                                        if (canDragSubcategories || canDragMenuItems) {
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
                                }
                            }
                        }
                    ) { paddingValues ->
                        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                            // Display breadcrumb trail
                            Surface(
                                color = MaterialTheme.colorScheme.surface,
                                shadowElevation = 2.dp
                            ) {
                                BreadcrumbTrail(
                                    categories = breadcrumbCategories,
                                    onCategoryClick = { category ->
                                        // Find the category with items and navigate to it
//                                        val categoryWithItems = uiState.allCategories.find { it.category.id == category.id }
//                                        if (categoryWithItems != null) {
//                                            onCategoryClicked(categoryWithItems)
//                                        }
                                    }
                                )
                            }
                            // Display subcategories section if there are any
                            if (currentCategory?.subcategories?.isNotEmpty() == true) {
//                                Text(
//                                    text = "Subcategories",
//                                    fontSize = 18.sp,
//                                    fontWeight = FontWeight.Bold,
//                                    modifier = Modifier.padding(8.dp)
//                                )

                                LazyVerticalGrid(
                                    state = subcategoriesGridState,
                                    columns = GridCells.Adaptive(120.dp),
                                    contentPadding = PaddingValues(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(updatedSubcategories, key = { it.id }) { subcategory ->
                                        ReorderableItem(
                                            reorderableSubcategoriesGridState,
                                            key = subcategory.id,
                                            modifier = Modifier.padding(2.dp)
                                        ) {
                                            // Find the full subcategory with its items from the allCategories list
                                            val subcategoryWithItems = uiState.allCategories.find { it.category.id == subcategory.id }
                                            if (subcategoryWithItems != null) {
                                                CategoryComponent(
                                                    text = subcategory.name,
                                                    modifier = Modifier,
                                                    containerColor = subcategory.color,
                                                    onClick = { 
                                                        if (canDragSubcategories) {
                                                            // If in drag mode, do nothing
                                                        } else {
                                                            // If not in drag mode, either open drawer for editing or navigate
                                                            selectedSubcategory = subcategory
                                                            categoryDatabaseOperation = DatabaseOperation.Edit
                                                            scope.launch {
                                                                categoryDrawerState.open()
                                                            }
                                                        }
                                                    }
                                                )
                                                if (canDragSubcategories) {
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

                            // Display menu items section if there are any
                            if (currentCategory?.menuItems?.isNotEmpty() == true) {
//                                Text(
//                                    text = "Menu Items",
//                                    fontSize = 18.sp,
//                                    fontWeight = FontWeight.Bold,
//                                    modifier = Modifier.padding(8.dp)
//                                )

                                LazyVerticalGrid(
                                    state = menuItemsGridState,
                                    columns = GridCells.Adaptive(120.dp),
                                    contentPadding = PaddingValues(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(updatedMenuItems, key = { it.id }) { menuItem ->
                                        ReorderableItem(
                                            reorderableMenuItemsGridState,
                                            key = menuItem.id,
                                            modifier = Modifier.padding(2.dp)
                                        ) {
                                            MenuItemComponent(
                                                title = menuItem.name,
                                                modifier = Modifier,
                                                onClick = { 
                                                    if (canDragMenuItems) {
                                                        // If in drag mode, do nothing
                                                    } else {
                                                        // If not in drag mode, open drawer for editing
                                                        selectedMenuItem = menuItem
                                                        menuItemDatabaseOperation = DatabaseOperation.Edit
                                                        scope.launch {
                                                            menuItemDrawerState.open()
                                                        }
                                                    }
                                                }
                                            )
                                            if (canDragMenuItems) {
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
                    }
                },
                leftContent = {}
            )
        },
        leftContent = {}
    )
}
