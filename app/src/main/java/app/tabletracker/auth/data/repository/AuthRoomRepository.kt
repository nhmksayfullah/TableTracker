package app.tabletracker.auth.data.repository

import app.tabletracker.auth.data.local.AuthDao
import app.tabletracker.auth.data.model.Restaurant
import app.tabletracker.auth.domain.repository.AuthRepository
import app.tabletracker.common.data.RestaurantExtra
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