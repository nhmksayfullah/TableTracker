package app.tabletracker.feature_order.ui

import app.tabletracker.auth.data.model.Restaurant
import app.tabletracker.common.data.RestaurantExtra
import app.tabletracker.feature_menu.data.entity.CategoryWithMenuItems
import app.tabletracker.feature_order.data.entity.OrderStatus
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems
import app.tabletracker.feature_order.ui.section.dummyRestaurant

data class OrderUiState(
    val menus: List<CategoryWithMenuItems> = emptyList(),
    val todayOrders: List<OrderWithOrderItems> = emptyList(),
    val currentOrder: OrderWithOrderItems? = null,
    val runningOrders: List<OrderWithOrderItems> = todayOrders.filter {
        it.order.orderStatus == OrderStatus.Running
    },
    val completedOrders: List<OrderWithOrderItems> = todayOrders.filter {
        it.order.orderStatus == OrderStatus.Completed
    },
    val cancelledOrders: List<OrderWithOrderItems> = todayOrders.filter {
        it.order.orderStatus == OrderStatus.Cancelled
    },
    val restaurantInfo: Restaurant? = null,
    val restaurantExtra: RestaurantExtra? = null
)
