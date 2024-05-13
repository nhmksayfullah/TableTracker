package app.tabletracker.settings.domain

import app.tabletracker.auth.data.model.Restaurant
import app.tabletracker.common.data.RestaurantExtra
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun readRestaurantInfo(): Flow<Restaurant>
    suspend fun upsertRestaurantExtra(restaurantExtra: RestaurantExtra)
    fun readRestaurantExtra(restaurantId: String): Flow<RestaurantExtra>
}