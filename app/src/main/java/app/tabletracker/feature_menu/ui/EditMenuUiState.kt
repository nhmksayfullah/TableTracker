package app.tabletracker.feature_menu.ui

import app.tabletracker.feature_menu.data.entity.Category
import app.tabletracker.feature_menu.data.entity.CategoryWithMenuItems
import app.tabletracker.feature_menu.data.entity.MenuItem

data class EditMenuUiState(
    val categories: List<Category> = emptyList(),
    val menus: List<CategoryWithMenuItems> = emptyList(),
    val selectedCategory: Category = Category(name = ""),
    val selectedMenuItem: MenuItem = MenuItem(
        name = "",
        abbreviation = "",
        description = "",
        prices = mapOf(),
        categoryId = selectedCategory.id
    ),
)
