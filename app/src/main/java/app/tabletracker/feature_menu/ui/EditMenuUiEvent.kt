package app.tabletracker.feature_menu.ui

import app.tabletracker.feature_menu.data.entity.Category
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_order.data.entity.OrderType

sealed class EditMenuUiEvent {
    data class ChangeCategory(val category: Category) : EditMenuUiEvent()
    data class ChangeDetailsOfMenuItem(val menuItem: MenuItem) : EditMenuUiEvent()
    data class ChangePricesOfMenuItem(val pair: Pair<OrderType, Float>) : EditMenuUiEvent()
    data object AddNewCategory : EditMenuUiEvent()
    data object AddNewMenuItem : EditMenuUiEvent()
    data class UpsertCategory(val category: Category) : EditMenuUiEvent()
    data class UpsertCategories(val categories: List<Category>) : EditMenuUiEvent()
    data class UpsertMenuItem(val menuItem: MenuItem) : EditMenuUiEvent()

    data class DeleteCategory(val category: Category) : EditMenuUiEvent()
    data class DeleteMenuItem(val menuItem: MenuItem) : EditMenuUiEvent()
    data class ReorderCategories(val categories: List<Category>) : EditMenuUiEvent()
    data class ReorderMenuItems(val menuItems: List<MenuItem>) : EditMenuUiEvent()
}