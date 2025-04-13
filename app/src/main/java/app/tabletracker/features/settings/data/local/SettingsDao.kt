package app.tabletracker.features.settings.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import app.tabletracker.core.model.RestaurantExtra
import app.tabletracker.features.auth.data.model.Restaurant
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM restaurant LIMIT 1")
    fun readRestaurantInfo(): Flow<Restaurant>

    @Upsert
    suspend fun upsertRestaurantExtra(restaurantExtra: RestaurantExtra)

    @Query("SELECT * FROM restaurantextra WHERE restaurantId = :restaurantId LIMIT 1")
    fun readRestaurantExtra(restaurantId: String): Flow<RestaurantExtra>
}