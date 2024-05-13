package app.tabletracker.feature_menu.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.tabletracker.util.generateInstantTime

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val creationTime: Long = generateInstantTime()
)
