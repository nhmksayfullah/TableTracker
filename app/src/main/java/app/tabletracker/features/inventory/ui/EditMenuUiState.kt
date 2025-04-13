package app.tabletracker.features.inventory.ui

import app.tabletracker.features.inventory.data.entity.Category
import app.tabletracker.features.inventory.data.entity.CategoryWithMenuItems
import app.tabletracker.features.inventory.data.entity.MenuItem


data class EditMenuUiState(
    val categories: List<Category> = emptyList(),
    val menus: List<CategoryWithMenuItems> = emptyList(),
    val selectedCategory: Category? = null,
    val selectedMenuItem: MenuItem? = null,
)
