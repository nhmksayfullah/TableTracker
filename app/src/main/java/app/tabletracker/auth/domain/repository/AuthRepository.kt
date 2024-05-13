package app.tabletracker.auth.domain.repository

import app.tabletracker.auth.data.model.Restaurant
import app.tabletracker.common.data.RestaurantExtra
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun registerRestaurant(restaurant: Restaurant)
    fun readRestaurantInfo(): Flow<Restaurant>
}