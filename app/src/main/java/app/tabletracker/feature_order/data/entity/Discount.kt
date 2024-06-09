package app.tabletracker.feature_order.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.tabletracker.util.generateUniqueId
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
@Entity
data class Discount(
    @PrimaryKey
    val id: String = generateUniqueId(),
    val title: String,
    val value: String
)
