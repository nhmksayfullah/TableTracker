package app.tabletracker.feature_order.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.tabletracker.feature_menu.data.entity.MenuItem

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
