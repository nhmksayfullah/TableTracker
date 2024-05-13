package app.tabletracker.common.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RestaurantExtra(
    @PrimaryKey(autoGenerate = false)
    val restaurantId: String,
    val totalTable: Int
)
