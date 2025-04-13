package app.tabletracker.features.settings.data.repository

import app.tabletracker.features.auth.data.model.Restaurant
import app.tabletracker.core.model.RestaurantExtra
import app.tabletracker.features.settings.data.local.SettingsDao
import app.tabletracker.features.settings.domain.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRoomRepository(private val settingsDao: SettingsDao) : SettingsRepository {

    override fun readRestaurantInfo(): Flow<Restaurant> {
        return settingsDao.readRestaurantInfo()
    }

    override suspend fun upsertRestaurantExtra(restaurantExtra: RestaurantExtra) {
        settingsDao.upsertRestaurantExtra(restaurantExtra)
    }

    override fun readRestaurantExtra(restaurantId: String): Flow<RestaurantExtra> {
        return settingsDao.readRestaurantExtra(restaurantId)
    }
}