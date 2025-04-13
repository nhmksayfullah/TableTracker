package app.tabletracker.features.inventory.data.entity.meal

import app.tabletracker.features.inventory.data.entity.MenuItem
import app.tabletracker.util.generateUniqueId
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class MealCourse(
    val id: String = generateUniqueId(),
    val name: String = "",
    val availableItems: List<MenuItem> = emptyList(),
    val selectedItem: MenuItem? = null
)
