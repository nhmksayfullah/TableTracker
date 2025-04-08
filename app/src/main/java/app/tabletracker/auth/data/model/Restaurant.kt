package app.tabletracker.auth.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.tabletracker.util.generateUniqueId
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Restaurant(
    @PrimaryKey(autoGenerate = false)
    val id: String = generateUniqueId(),
    val name: String,
    val address: String,
    val contactNumber: String,
    val vatNumber: String,
    val website: String? = null,
    val licence: String
)