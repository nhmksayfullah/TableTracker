package app.tabletracker.feature_menu.data.entity.meal

import app.tabletracker.feature_menu.data.entity.MenuItem
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Meal(
    val sections: List<MealCourse> = emptyList()
)
@JsonClass(generateAdapter = true)
data class MealCourse(
    val id: String,
    val name: String,
    val availableItems: List<MenuItem>
)
