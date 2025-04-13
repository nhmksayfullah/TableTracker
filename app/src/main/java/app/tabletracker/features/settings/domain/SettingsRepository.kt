package app.tabletracker.features.settings.domain

import app.tabletracker.features.auth.data.model.Restaurant
import app.tabletracker.core.model.RestaurantExtra
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun readRestaurantInfo(): Flow<Restaurant>
    suspend fun upsertRestaurantExtra(restaurantExtra: RestaurantExtra)
    fun readRestaurantExtra(restaurantId: String): Flow<RestaurantExtra>
}