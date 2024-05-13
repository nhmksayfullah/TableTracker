package app.tabletracker.feature_order.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class OrderWithOrderItems(
    @Embedded val order: Order,
    @Relation(
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val orderItems: List<OrderItem>
)
