package app.tabletracker.feature_order.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import app.tabletracker.auth.data.model.Restaurant
import app.tabletracker.common.data.RestaurantExtra
import app.tabletracker.feature_menu.data.entity.CategoryWithMenuItems
import app.tabletracker.feature_order.data.entity.Discount
import app.tabletracker.feature_order.data.entity.Order
import app.tabletracker.feature_order.data.entity.OrderItem
import app.tabletracker.feature_order.data.entity.OrderStatus
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Transaction
    @Query("SELECT * FROM category")
    fun readAllCategoriesWithMenuItems(): Flow<List<CategoryWithMenuItems>>

    @Transaction
    @Query("SELECT * FROM `order`")
    fun readAllOrdersWithOrderItems(): Flow<List<OrderWithOrderItems>>

    @Transaction
    @Query("SELECT * FROM `order` WHERE orderStatus = :orderStatus")
    fun readAllOrdersWithOrderItems(orderStatus: OrderStatus): Flow<List<OrderWithOrderItems>>

    @Transaction
    @Query("SELECT * FROM `order` WHERE id = :orderId")
    fun readOrderWithOrderItems(orderId: String): Flow<OrderWithOrderItems>

    @Query("SELECT * FROM `order`")
    fun readAllOrders(): Flow<List<Order>>

    @Transaction
    @Query("SELECT * FROM `order` ORDER BY creationTime DESC LIMIT 1")
    fun readLastAddedOrder(): Flow<OrderWithOrderItems>

    @Transaction
    @Query("SELECT * FROM `order` WHERE creationTime >= :startOfDay AND creationTime < :endOfDay")
    fun readOrdersCreatedToday(startOfDay: Long, endOfDay: Long): Flow<List<OrderWithOrderItems>>

    @Upsert
    suspend fun writeOrder(order: Order)
    @Upsert
    suspend fun writeOrderItem(orderItem: OrderItem)
    @Delete
    suspend fun deleteOrder(order: Order)
    @Delete
    suspend fun deleteOrderItem(orderItem: OrderItem)


    @Query("SELECT * FROM restaurant LIMIT 1")
    fun readRestaurantInfo(): Flow<Restaurant>

    @Query("SELECT * FROM restaurantextra WHERE restaurantId = :restaurantId LIMIT 1")
    fun readRestaurantExtra(restaurantId: String): Flow<RestaurantExtra>


    @Upsert
    fun writeDiscount(discount: Discount)

    @Delete
    fun deleteDiscount(discount: Discount)

    @Query("SELECT * FROM discount")
    fun readDiscounts(): Flow<List<Discount>>


}