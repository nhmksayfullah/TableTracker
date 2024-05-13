package app.tabletracker.settings.ui

import app.tabletracker.common.data.RestaurantExtra

sealed class SettingsUiEvent {
    data class UpdateRestaurantExtra(val restaurantExtra: RestaurantExtra): SettingsUiEvent()
}