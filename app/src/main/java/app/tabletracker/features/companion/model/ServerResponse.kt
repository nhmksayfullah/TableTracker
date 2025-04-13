package app.tabletracker.features.companion.model

import app.tabletracker.core.model.RestaurantExtra
import app.tabletracker.features.auth.data.model.Restaurant
import app.tabletracker.features.inventory.data.entity.CategoryWithMenuItems
import app.tabletracker.features.order.data.entity.Order
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