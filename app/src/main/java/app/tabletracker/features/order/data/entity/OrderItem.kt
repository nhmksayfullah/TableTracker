package app.tabletracker.features.order.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.tabletracker.features.inventory.data.entity.MenuItem
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val menuItem: MenuItem,
    val quantity: Int = 1,
    val addedNote: String = "",
    val orderItemStatus: OrderItemStatus = OrderItemStatus.Added,
    val orderId: String
)

fun MenuItem.toOrderItem(orderId: String): OrderItem {
    return OrderItem(
        menuItem = this,
        orderId = orderId
    )
}
