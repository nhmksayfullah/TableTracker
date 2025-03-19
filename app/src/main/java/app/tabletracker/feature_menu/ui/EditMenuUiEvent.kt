package app.tabletracker.feature_menu.ui

import app.tabletracker.feature_menu.data.entity.Category
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_order.data.entity.OrderType

sealed class EditMenuUiEvent {
    data class UpsertCategory(val category: Category) : EditMenuUiEvent()
    data class UpsertMenuItem(val menuItem: MenuItem) : EditMenuUiEvent()

    data class DeleteCategory(val category: Category) : EditMenuUiEvent()
    data class DeleteMenuItem(val menuItem: MenuItem) : EditMenuUiEvent()

    data class SetSelectedCategory(val category: Category) : EditMenuUiEvent()
    data class SetSelectedMenuItem(val menuItem: MenuItem) : EditMenuUiEvent()

    data class ReorderCategories(val categories: List<Category>) : EditMenuUiEvent()
    data class ReorderMenuItems(val menuItems: List<MenuItem>) : EditMenuUiEvent()
}