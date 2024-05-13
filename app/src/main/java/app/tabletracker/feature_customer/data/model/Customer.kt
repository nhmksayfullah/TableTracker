package app.tabletracker.feature_customer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.tabletracker.util.generateUniqueId
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity
data class Customer(
    @PrimaryKey(autoGenerate = false)
    val id: String = generateUniqueId(),
    val name: String,
    val contact: String,
    val postCode: String,
    val houseNumber: String,
    val street: String,
)
