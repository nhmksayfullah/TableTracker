package app.tabletracker.features.settings.ui

import app.tabletracker.core.model.RestaurantExtra
import app.tabletracker.features.auth.data.model.Restaurant

data class SettingsUiState(
    val restaurantInfo: Restaurant? = null,
    val restaurantExtra: RestaurantExtra? = null
)