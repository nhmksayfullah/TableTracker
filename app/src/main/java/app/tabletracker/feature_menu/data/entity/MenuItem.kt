package app.tabletracker.feature_menu.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.tabletracker.feature_menu.data.entity.meal.MealCourse
import app.tabletracker.feature_order.data.entity.OrderType
import app.tabletracker.util.generateInstantTime
import app.tabletracker.util.generateUniqueId
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity
data class MenuItem(
    @PrimaryKey(autoGenerate = false)
    val id: String = generateUniqueId(),
    val name: String,
    val abbreviation: String,
    val description: String = "",
    val prices: Map<OrderType, Float>,
    val categoryId: Int,
    @ColumnInfo(name = "isKitchenCategory", defaultValue = "true")
    val isKitchenCategory: Boolean = true,
    val creationTime: Long = generateInstantTime(),
    @ColumnInfo(name = "isMeal", defaultValue = "false")
    val isMeal: Boolean = false,
    @ColumnInfo(name = "mealCourses", defaultValue = "[]")
    val mealCourses: List<MealCourse> = emptyList()
)