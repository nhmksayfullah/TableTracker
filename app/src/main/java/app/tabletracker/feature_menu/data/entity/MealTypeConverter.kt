package app.tabletracker.feature_menu.data.entity

import androidx.room.TypeConverter
import app.tabletracker.feature_menu.data.entity.meal.MealCourse
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter

@OptIn(ExperimentalStdlibApi::class)
class MealTypeConverter {
    private val moshi: Moshi by lazy { Moshi.Builder().build() }

    @TypeConverter
    fun fromMeal(meal: List<MealCourse>): String {
        val adapter = moshi.adapter<List<MealCourse>>()
        return adapter.toJson(meal)
    }

    @TypeConverter
    fun toMeal(json: String): List<MealCourse>? {
        val adapter = moshi.adapter<List<MealCourse>>()
        return adapter.fromJson(json)
    }
}