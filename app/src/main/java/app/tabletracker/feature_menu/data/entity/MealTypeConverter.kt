package app.tabletracker.feature_menu.data.entity

import androidx.room.TypeConverter
import app.tabletracker.feature_menu.data.entity.meal.Meal
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter

@OptIn(ExperimentalStdlibApi::class)
class MealTypeConverter {
    private val moshi : Moshi by lazy { Moshi.Builder().build() }
    @TypeConverter
    fun fromMeal(meal: Meal): String {
        val adapter = moshi.adapter<Meal>()
        return adapter.toJson(meal)
    }
    @TypeConverter
    fun toMeal(json: String): Meal? {
        val adapter = moshi.adapter<Meal>()
        return adapter.fromJson(json)
    }
}