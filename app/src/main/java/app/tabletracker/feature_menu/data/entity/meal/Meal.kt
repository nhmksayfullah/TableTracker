package app.tabletracker.feature_menu.data.entity.meal

import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.util.generateUniqueId
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class MealCourse(
    val id: String = generateUniqueId(),
    val name: String = "",
    val availableItems: List<MenuItem> = emptyList()
)
