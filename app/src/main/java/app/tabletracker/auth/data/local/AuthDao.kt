package app.tabletracker.auth.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import app.tabletracker.auth.data.model.Restaurant
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthDao {

    @Upsert
    suspend fun registerRestaurant(restaurant: Restaurant)

    @Query("SELECT * FROM restaurant LIMIT 1")
    fun readRestaurantInfo(): Flow<Restaurant>

//    @Upsert
//    suspend fun upsertRestaurantExtra(restaurantExtra: RestaurantExtra)
}