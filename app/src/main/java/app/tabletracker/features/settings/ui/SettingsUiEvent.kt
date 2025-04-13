package app.tabletracker.features.settings.ui

import app.tabletracker.core.model.RestaurantExtra

sealed class SettingsUiEvent {
    data class UpdateRestaurantExtra(val restaurantExtra: RestaurantExtra) : SettingsUiEvent()
}