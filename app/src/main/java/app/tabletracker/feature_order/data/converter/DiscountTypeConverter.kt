package app.tabletracker.feature_order.data.converter

import androidx.room.TypeConverter
import app.tabletracker.feature_order.data.entity.Discount
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter

@OptIn(ExperimentalStdlibApi::class)
class DiscountTypeConverter {
    private val moshi by lazy { Moshi.Builder().build() }

    @TypeConverter
    fun fromDiscount(discount: Discount): String {
        val adapter = moshi.adapter<Discount>()
        return adapter.toJson(discount)
    }
    @TypeConverter
    fun toDiscount(json: String): Discount? {
        val adapter = moshi.adapter<Discount>()
        return adapter.fromJson(json)
    }

}