package app.tabletracker.feature_order.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.tabletracker.feature_customer.data.model.Customer
import app.tabletracker.util.generateInstantTime
import app.tabletracker.util.generateUniqueId
import kotlinx.serialization.Serializable


// The receipt order is: Condiment, Starters, Main course, side dishes, Extras
@Serializable
@Entity
data class Order(
    @PrimaryKey(autoGenerate = false)
    val id: String = generateUniqueId(),
    val orderNumber: Int,
    val orderType: OrderType,
    val tableNumber: Int? = null,
    val paymentMethod: PaymentMethod = PaymentMethod.None,
    val orderStatus: OrderStatus = OrderStatus.Created,
    val totalPrice: Float = 0.0f,
    val creationTime: Long = generateInstantTime(),
    val customer: Customer? = null,
    @ColumnInfo(name = "discount", defaultValue = "null")
    val discount: Discount? = null,
    @ColumnInfo(name = "totalPerson", defaultValue = "null")
    val totalPerson: Int? = null,
    @ColumnInfo(defaultValue = "'Main'")
    val orderSource: OrderSource = OrderSource.Main
)