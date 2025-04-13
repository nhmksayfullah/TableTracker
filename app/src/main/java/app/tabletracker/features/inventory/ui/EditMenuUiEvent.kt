package app.tabletracker.features.inventory.ui

import app.tabletracker.features.inventory.data.entity.Category
import app.tabletracker.features.inventory.data.entity.MenuItem
import app.tabletracker.features.order.data.entity.OrderType

sealed class EditMenuUiEvent {
    data class UpsertCategory(val category: Category) : EditMenuUiEvent()
    data class UpsertMenuItem(val menuItem: MenuItem, val newPrices: Map<OrderType, Float>) :
        EditMenuUiEvent()

    data class DeleteCategory(val category: Category) : EditMenuUiEvent()
    data class DeleteMenuItem(val menuItem: MenuItem) : EditMenuUiEvent()

    data class SetSelectedCategory(val category: Category) : EditMenuUiEvent()
    data class SetSelectedMenuItem(val menuItem: MenuItem) : EditMenuUiEvent()
    data class UpdateSelectedMenuItem(val menuItem: MenuItem) : EditMenuUiEvent()

    data class ReorderCategories(val categories: List<Category>) : EditMenuUiEvent()
    data class ReorderMenuItems(val menuItems: List<MenuItem>) : EditMenuUiEvent()
}