package app.tabletracker.common.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class RestaurantExtra(
    @PrimaryKey(autoGenerate = false)
    val restaurantId: String,
    val totalTable: Int
)
