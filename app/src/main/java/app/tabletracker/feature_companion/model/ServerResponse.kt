package app.tabletracker.feature_companion.model

import app.tabletracker.auth.data.model.Restaurant
import app.tabletracker.common.data.RestaurantExtra
import app.tabletracker.feature_menu.data.entity.CategoryWithMenuItems
import app.tabletracker.feature_order.data.entity.Order
import kotlinx.serialization.Serializable

@Serializable
sealed class ServerResponse {
    @Serializable
    data class RestaurantInfo(val restaurant: Restaurant, val restaurantExtra: RestaurantExtra): ServerResponse()
    @Serializable
    data class Menu(val menu: List<CategoryWithMenuItems>): ServerResponse()
    @Serializable
    data class OrderInfo(val order: Order): ServerResponse()
}