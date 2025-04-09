package app.tabletracker.auth.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import app.tabletracker.auth.data.model.Restaurant
import app.tabletracker.common.data.RestaurantExtra
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthDao {

    @Upsert
    suspend fun registerRestaurant(restaurant: Restaurant)

    @Upsert
    suspend fun upsertRestaurantExtra(restaurantExtra: RestaurantExtra)

    @Query("SELECT * FROM restaurant LIMIT 1")
    fun readRestaurantInfo(): Flow<Restaurant>

    @Query("SELECT * FROM restaurantextra WHERE restaurantId = :restaurantId LIMIT 1")
    fun readRestaurantExtra(restaurantId: String): Flow<RestaurantExtra>

    @Query("SELECT EXISTS(SELECT 1 FROM menuitem)")
    fun hasInventory(): Flow<Boolean>
}