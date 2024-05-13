package app.tabletracker.feature_order.data.repository

import app.tabletracker.auth.data.model.Restaurant
import app.tabletracker.common.data.RestaurantExtra
import app.tabletracker.feature_menu.data.entity.CategoryWithMenuItems
import app.tabletracker.feature_order.data.entity.Order
import app.tabletracker.feature_order.data.entity.OrderItem
import app.tabletracker.feature_order.data.entity.OrderStatus
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems
import app.tabletracker.feature_order.data.local.OrderDao
import app.tabletracker.feature_order.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow

class OrderRoomRepository(private val orderDao: OrderDao): OrderRepository {
    override fun readAllCategoriesWithMenuItems(): Flow<List<CategoryWithMenuItems>> {
        return orderDao.readAllCategoriesWithMenuItems()
    }

    override fun readAllOrdersWithOrderItems(): Flow<List<OrderWithOrderItems>> {
        return orderDao.readAllOrdersWithOrderItems()
    }

    override fun readAllOrdersWithOrderItems(orderStatus: OrderStatus): Flow<List<OrderWithOrderItems>> {
        return orderDao.readAllOrdersWithOrderItems(orderStatus)
    }

    override fun readOrderWithOrderItems(orderId: String): Flow<OrderWithOrderItems> {
        return orderDao.readOrderWithOrderItems(orderId)
    }

    override fun readAllOrders(): Flow<List<Order>> {
        return orderDao.readAllOrders()
    }

    override fun readLastAddedOrder(): Flow<OrderWithOrderItems> {
        return orderDao.readLastAddedOrder()
    }

    override fun readOrdersCreatedToday(
        startOfDay: Long,
        endOfDay: Long
    ): Flow<List<OrderWithOrderItems>> {
        return orderDao.readOrdersCreatedToday(startOfDay, endOfDay)
    }

    override suspend fun writeOrder(order: Order) {
        orderDao.writeOrder(order)
    }

    override suspend fun writeOrderItem(orderItem: OrderItem) {
        orderDao.writeOrderItem(orderItem)
    }

    override suspend fun deleteOrder(order: Order) {
        orderDao.deleteOrder(order)
    }

    override suspend fun deleteOrderItem(orderItem: OrderItem) {
        orderDao.deleteOrderItem(orderItem)
    }


    override fun readRestaurantInfo(): Flow<Restaurant> {
        return orderDao.readRestaurantInfo()
    }

    override fun readRestaurantExtra(restaurantId: String): Flow<RestaurantExtra> {
        return orderDao.readRestaurantExtra(restaurantId)
    }
}