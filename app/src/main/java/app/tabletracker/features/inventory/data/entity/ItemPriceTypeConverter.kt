package app.tabletracker.features.inventory.data.entity

import androidx.room.TypeConverter
import app.tabletracker.features.order.data.entity.OrderType
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter

@OptIn(ExperimentalStdlibApi::class)
class ItemPriceTypeConverter {

    private val moshi: Moshi by lazy { Moshi.Builder().build() }

    @TypeConverter
    fun fromMap(map: Map<OrderType, Float>?): String? {
        val adapter = moshi.adapter<Map<OrderType, Float>>()
        return adapter.toJson(map)
    }

    @TypeConverter
    fun toMap(json: String?): Map<OrderType, Float>? {
        val adapter = moshi.adapter<Map<OrderType, Float>>()
        return json?.let { adapter.fromJson(it) }
    }

}