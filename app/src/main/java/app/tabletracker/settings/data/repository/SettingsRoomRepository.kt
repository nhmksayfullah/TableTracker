package app.tabletracker.settings.data.repository

import app.tabletracker.auth.data.model.Restaurant
import app.tabletracker.common.data.RestaurantExtra
import app.tabletracker.settings.data.local.SettingsDao
import app.tabletracker.settings.domain.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRoomRepository(private val settingsDao: SettingsDao): SettingsRepository {

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