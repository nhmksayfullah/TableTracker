package app.tabletracker.auth.data.repository

import app.tabletracker.auth.data.local.AuthDao
import app.tabletracker.auth.data.model.Restaurant
import app.tabletracker.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthRoomRepository(private val authDao: AuthDao) : AuthRepository {
    override suspend fun registerRestaurant(restaurant: Restaurant) {
        authDao.registerRestaurant(restaurant)
    }

    override fun readRestaurantInfo(): Flow<Restaurant> {
        return authDao.readRestaurantInfo()
    }
}