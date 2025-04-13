package app.tabletracker.features.auth.data.repository

import app.tabletracker.core.model.RestaurantExtra
import app.tabletracker.features.auth.data.local.AuthDao
import app.tabletracker.features.auth.data.model.Restaurant
import app.tabletracker.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthRoomRepository(private val authDao: AuthDao) : AuthRepository {
    override suspend fun registerRestaurant(restaurant: Restaurant) {
        authDao.registerRestaurant(restaurant)
    }

    override suspend fun upsertRestaurantExtra(restaurantExtra: RestaurantExtra) {
        authDao.upsertRestaurantExtra(restaurantExtra)
    }

    override fun readRestaurantInfo(): Flow<Restaurant> {
        return authDao.readRestaurantInfo()
    }

    override fun readRestaurantExtra(restaurantId: String): Flow<RestaurantExtra> {
        return authDao.readRestaurantExtra(restaurantId)
    }

    override fun hasInventory(): Flow<Boolean> {
        return authDao.hasInventory()
    }
}