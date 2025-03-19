package app.tabletracker.feature_customer.data.model

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter


@OptIn(ExperimentalStdlibApi::class)
class CustomerTypeConverter {
    private val moshi: Moshi by lazy { Moshi.Builder().build() }


    @TypeConverter
    fun fromCustomer(customer: Customer): String {
        val adapter = moshi.adapter<Customer>()
        return adapter.toJson(customer)
    }

    @TypeConverter
    fun toCustomer(json: String): Customer? {
        val adapter = moshi.adapter<Customer>()
        return adapter.fromJson(json)
    }
}