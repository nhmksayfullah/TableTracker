package app.tabletracker.auth.domain.repository

import app.tabletracker.auth.data.model.Restaurant
import app.tabletracker.common.data.RestaurantExtra
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun registerRestaurant(restaurant: Restaurant)
    suspend fun upsertRestaurantExtra(restaurantExtra: RestaurantExtra)
    fun readRestaurantInfo(): Flow<Restaurant>
    fun readRestaurantExtra(restaurantId: String): Flow<RestaurantExtra>
    fun hasInventory(): Flow<Boolean>
}