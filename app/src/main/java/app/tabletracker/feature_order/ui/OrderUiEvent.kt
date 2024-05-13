package app.tabletracker.feature_order.ui

import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_order.data.entity.Order
import app.tabletracker.feature_order.data.entity.OrderItem
import app.tabletracker.feature_order.data.entity.OrderItemStatus
import app.tabletracker.feature_order.data.entity.OrderType
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems
import app.tabletracker.feature_order.data.entity.PaymentMethod

sealed class OrderUiEvent {
    data class CreateNewOrder(val orderType: OrderType): OrderUiEvent()
    data class UpdateCurrentOrder(val order: Order): OrderUiEvent()

    data class AddItemToOrder(val menuItem: MenuItem, val orderId: String): OrderUiEvent()
    data class RemoveItemFromOrder(val orderItem: OrderItem): OrderUiEvent()
    data class UpdateOrderItem(val orderItem: OrderItem): OrderUiEvent()
    data class UpdateCurrentOrderItemsStatus(val orderItemStatus: OrderItemStatus): OrderUiEvent()

    data class UpdateCurrentOrderWithOrderItems(val orderId: String?): OrderUiEvent()
}