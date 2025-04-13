package app.tabletracker.features.order.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.tabletracker.util.generateUniqueId
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
@Entity
data class Discount(
    @PrimaryKey
    val id: String = generateUniqueId(),
    val title: String,
    val value: String
)
