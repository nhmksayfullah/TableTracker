package app.tabletracker.features.order.data.converter

import androidx.room.TypeConverter
import app.tabletracker.features.inventory.data.entity.MenuItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter


@OptIn(ExperimentalStdlibApi::class)
class MenuItemTypeConverter {
    private val moshi: Moshi by lazy { Moshi.Builder().build() }


    @TypeConverter
    fun fromMenuItem(menuItem: MenuItem): String {
        val adapter = moshi.adapter<MenuItem>()
        return adapter.toJson(menuItem)
    }

    @TypeConverter
    fun toMenuItem(json: String): MenuItem? {
        val adapter = moshi.adapter<MenuItem>()
        return adapter.fromJson(json)
    }
}