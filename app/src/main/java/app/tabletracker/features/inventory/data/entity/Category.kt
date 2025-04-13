package app.tabletracker.features.inventory.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.tabletracker.util.generateInstantTime
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    @ColumnInfo(name = "isKitchenCategory", defaultValue = "true")
    val isKitchenCategory: Boolean = true,
    val creationTime: Long = generateInstantTime(),
    @ColumnInfo(name = "index", defaultValue = "-1")
    val index: Int = -1,
    @ColumnInfo(name = "color", defaultValue = "-1")
    val color: Int = -1
)
