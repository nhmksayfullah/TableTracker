package app.tabletracker.feature_order.ui.state

import app.tabletracker.feature_customer.data.model.Customer
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_order.data.entity.Order
import app.tabletracker.feature_order.data.entity.OrderItem
import app.tabletracker.feature_order.data.entity.OrderItemStatus
import app.tabletracker.feature_order.data.entity.OrderType
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems

sealed class OrderUiEvent {
    data class CreateNewOrder(val orderType: OrderType): OrderUiEvent()
    data class UpdateCurrentOrder(val order: Order): OrderUiEvent()
    data class UpdateCustomer(val customer: Customer): OrderUiEvent()
    data class AddMenuItemToOrder(val menuItem: MenuItem): OrderUiEvent()
    data class RemoveItemFromOrder(val orderItem: OrderItem): OrderUiEvent()
    data class UpdateOrderItem(val orderItem: OrderItem): OrderUiEvent()
    data class SetCurrentOrderWithOrderItems(val orderWithOrderItems: OrderWithOrderItems?): OrderUiEvent()


    @Deprecated("Use [OrderUiEvent.UpdateCurrentOrder]")
    data class UpdateCurrentOrderItemsStatus(val orderItemStatus: OrderItemStatus): OrderUiEvent()
    @Deprecated("Use [OrderUiEvent.UpdateCurrentOrderWithOrderItems]")
    data class UpdateCurrentOrderWithOrderItems(val orderId: String?): OrderUiEvent()
    @Deprecated("Use [OrderUiEvent.UpdateCurrentOrderWithOrderItems and pass null]")
    data object UpdateCurrentOrderWithNull: OrderUiEvent()
    @Deprecated("Don't use this")
    data object PopulateLatestOrder: OrderUiEvent()

}