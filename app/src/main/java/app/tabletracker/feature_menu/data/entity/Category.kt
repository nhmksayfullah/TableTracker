package app.tabletracker.feature_menu.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.tabletracker.util.generateInstantTime

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    @ColumnInfo(name = "isKitchenCategory", defaultValue = "true")
    val isKitchenCategory: Boolean = true,
    val creationTime: Long = generateInstantTime()
)
