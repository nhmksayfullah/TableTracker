package app.tabletracker.features.order.domain.repository

import app.tabletracker.core.model.RestaurantExtra
import app.tabletracker.features.auth.data.model.Restaurant
import app.tabletracker.features.inventory.data.entity.CategoryWithMenuItems
import app.tabletracker.features.order.data.entity.Discount
import app.tabletracker.features.order.data.entity.Order
import app.tabletracker.features.order.data.entity.OrderItem
import app.tabletracker.features.order.data.entity.OrderStatus
import app.tabletracker.features.order.data.entity.OrderWithOrderItems
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun readAllCategoriesWithMenuItems(): Flow<List<CategoryWithMenuItems>>
    fun readAllOrdersWithOrderItems(): Flow<List<OrderWithOrderItems>>
    fun readAllOrdersWithOrderItems(orderStatus: OrderStatus): Flow<List<OrderWithOrderItems>>
    fun readOrderWithOrderItems(orderId: String): Flow<OrderWithOrderItems>
    fun readAllOrders(): Flow<List<Order>>
    fun readLastAddedOrder(): Flow<OrderWithOrderItems>
    fun readOrdersCreatedToday(startOfDay: Long, endOfDay: Long): Flow<List<OrderWithOrderItems>>

    suspend fun writeOrder(order: Order)
    suspend fun writeOrderItem(orderItem: OrderItem)
    suspend fun deleteOrder(order: Order)
    suspend fun deleteOrderItem(orderItem: OrderItem)

    fun readRestaurantInfo(): Flow<Restaurant>
    fun readRestaurantExtra(restaurantId: String): Flow<RestaurantExtra>

    fun writeDiscount(discount: Discount)
    fun deleteDiscount(discount: Discount)
    fun readDiscounts(): Flow<List<Discount>>

}