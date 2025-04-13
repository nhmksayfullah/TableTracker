package app.tabletracker.features.order.ui.state

import app.tabletracker.core.model.RestaurantExtra
import app.tabletracker.features.auth.data.model.Restaurant
import app.tabletracker.features.inventory.data.entity.CategoryWithMenuItems
import app.tabletracker.features.order.data.entity.OrderWithOrderItems

data class OrderUiState(
    val menus: List<CategoryWithMenuItems> = emptyList(),
    val todayOrders: List<OrderWithOrderItems> = emptyList(),
    val runningOrders: List<OrderWithOrderItems> = emptyList(),
    val completedOrders: List<OrderWithOrderItems> = emptyList(),
    val cancelledOrders: List<OrderWithOrderItems> = emptyList(),

    val currentOrder: OrderWithOrderItems? = null,
    val restaurantInfo: Restaurant? = null,
    val restaurantExtra: RestaurantExtra? = null,
    val currentOrderLocked: Boolean = false
)
