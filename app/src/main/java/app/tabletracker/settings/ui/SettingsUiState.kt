package app.tabletracker.settings.ui

import app.tabletracker.auth.data.model.Restaurant
import app.tabletracker.common.data.RestaurantExtra

data class SettingsUiState(
    val restaurantInfo: Restaurant? = null,
    val restaurantExtra: RestaurantExtra? = null
)