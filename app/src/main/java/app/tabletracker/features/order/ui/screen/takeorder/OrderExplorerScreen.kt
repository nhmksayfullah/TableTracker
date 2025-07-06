package app.tabletracker.features.order.ui.screen.takeorder

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.tabletracker.core.ui.component.CategoryComponent
import app.tabletracker.core.ui.component.MenuItemComponent
import app.tabletracker.features.inventory.data.entity.Category
import app.tabletracker.features.inventory.data.entity.CategoryWithMenuItems
import app.tabletracker.features.inventory.data.entity.MenuItem
import app.tabletracker.features.order.ui.state.OrderViewModel

/**
 * Builds a list of categories representing the breadcrumb trail from root to the current category
 */
private fun buildBreadcrumbTrail(
    categoryId: Int,
    allCategories: List<CategoryWithMenuItems>
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
fun ExplorerBreadcrumbTrail(
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
        // Always show "Menu" as the root
        Text(
            text = "Menu",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (categories.isEmpty()) FontWeight.Bold else FontWeight.Normal,
            color = if (categories.isEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.clickable { 
                // Go back to root menu
                onCategoryClick(Category(id = -1, name = "Menu")) 
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Show breadcrumb trail for categories
        categories.forEachIndexed { index, category ->
            Text(
                text = " > ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (index == categories.size - 1) FontWeight.Bold else FontWeight.Normal,
                color = if (index == categories.size - 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.clickable { onCategoryClick(category) },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun OrderExplorerScreen(
    viewModel: OrderViewModel,
    modifier: Modifier = Modifier,
    onMenuItemClicked: (MenuItem) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // State to track the current category ID
    var currentCategoryId by remember { mutableStateOf<Int?>(null) }

    // Get all categories
    val allCategories = uiState.menus

    // Find the current category
    val currentCategory = if (currentCategoryId != null) {
        allCategories.find { it.category.id == currentCategoryId }
    } else {
        null
    }

    // Build the breadcrumb trail
    val breadcrumbCategories = remember(currentCategoryId, allCategories) {
        if (currentCategoryId != null) {
            buildBreadcrumbTrail(currentCategoryId!!, allCategories)
        } else {
            emptyList()
        }
    }

    // Get subcategories for the current category
    val subcategories = remember(currentCategoryId, allCategories) {
        if (currentCategoryId == null) {
            // If no category is selected, show top-level categories (those with no parent)
            allCategories.filter { it.category.parentCategoryId == null }
        } else {
            // If a category is selected, show its subcategories
            allCategories.filter { it.category.parentCategoryId == currentCategoryId }
        }
    }

    // Get menu items for the current category
    val menuItems = remember(currentCategoryId, allCategories) {
        currentCategory?.menuItems ?: emptyList()
    }

    // Handle back navigation
    BackHandler {
        // Find the current category to get its parent
        val category = allCategories.find { it.category.id == currentCategoryId }
        // Navigate to parent or root
        currentCategoryId = category?.category?.parentCategoryId
    }

    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        // Breadcrumb navigation
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            ExplorerBreadcrumbTrail(
                categories = breadcrumbCategories,
                onCategoryClick = { category ->
                    // If clicking on the root, go back to top-level categories
                    if (category.id == -1) {
                        currentCategoryId = null
                    } else {
                        // Otherwise, navigate to the selected category
                        currentCategoryId = category.id
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Display subcategories
        if (subcategories.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(120.dp),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(subcategories) { categoryWithItems ->
                    Box(
                        modifier = Modifier.padding(2.dp)
                    ) {
                        CategoryComponent(
                            text = categoryWithItems.category.name,
                            containerColor = categoryWithItems.category.color,
                            onClick = {
                                // Navigate to this subcategory
                                currentCategoryId = categoryWithItems.category.id
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Display menu items for the current category
        if (menuItems.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(120.dp),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(menuItems) { menuItem ->
                    Box(
                        modifier = Modifier.padding(2.dp)
                    ) {
                        MenuItemComponent(
                            title = menuItem.name,
                            containerColor = menuItem.color,
                            onClick = {
                                onMenuItemClicked(menuItem)
                            }
                        )
                    }
                }
            }
        }

        // Show message if no items or subcategories
        if (subcategories.isEmpty() && menuItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (currentCategoryId == null) 
                        "No categories available" 
                    else 
                        "No items or subcategories available",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
