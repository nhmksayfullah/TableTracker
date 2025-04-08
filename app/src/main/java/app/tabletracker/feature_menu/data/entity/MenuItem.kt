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
    val mealCourses: List<MealCourse> = emptyList(),
    @ColumnInfo(name = "index", defaultValue = "-1")
    val index: Int = -1,
    @ColumnInfo(name = "color", defaultValue = "-1")
    val color: Int = -1

) {
    companion object {
        fun empty(categoryId: Int): MenuItem {
            return MenuItem(
                name = "",
                abbreviation = "",
                prices = emptyMap(),
                categoryId = categoryId,
            )
        }
    }
}


fun MenuItem.withUpdatedPrice(orderType: OrderType, newPrice: Float): MenuItem {
    // Create a new map with the updated price for the specified order type
    val updatedPrices = prices.toMutableMap().apply {
        this[orderType] = newPrice
    }

    // Return a new instance of MenuItem with the updated prices
    return this.copy(prices = updatedPrices)
}

fun MenuItem.withUpdatedPrices(newPrices: Map<OrderType, Float>): MenuItem {
    // Return a new instance of MenuItem with the updated prices
    return this.copy(prices = newPrices)
}