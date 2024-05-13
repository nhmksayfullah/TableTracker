package app.tabletracker.feature_menu.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithMenuItems(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val menuItems: List<MenuItem>
)
