package app.tabletracker.features.auth.domain.repository

import app.tabletracker.core.model.RestaurantExtra
import app.tabletracker.features.auth.data.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun registerRestaurant(restaurant: Restaurant)
    suspend fun upsertRestaurantExtra(restaurantExtra: RestaurantExtra)
    fun readRestaurantInfo(): Flow<Restaurant>
    fun readRestaurantExtra(restaurantId: String): Flow<RestaurantExtra>
    fun hasInventory(): Flow<Boolean>
}