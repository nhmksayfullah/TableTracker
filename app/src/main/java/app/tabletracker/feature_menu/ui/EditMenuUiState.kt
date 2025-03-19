package app.tabletracker.feature_menu.ui

import app.tabletracker.feature_menu.data.entity.Category
import app.tabletracker.feature_menu.data.entity.CategoryWithMenuItems
import app.tabletracker.feature_menu.data.entity.MenuItem

data class EditMenuUiState(
    val categories: List<Category> = emptyList(),
    val menus: List<CategoryWithMenuItems> = emptyList(),
    val selectedCategory: Category? = null,
    val selectedMenuItem: MenuItem? = null,
)
