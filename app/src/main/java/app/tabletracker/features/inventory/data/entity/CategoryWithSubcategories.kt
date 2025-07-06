package app.tabletracker.features.inventory.data.entity

import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.serialization.Serializable

@Serializable
data class CategoryWithSubcategories(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentCategoryId"
    )
    val subcategories: List<Category>
)