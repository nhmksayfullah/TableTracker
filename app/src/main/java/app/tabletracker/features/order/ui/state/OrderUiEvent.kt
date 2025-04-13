package app.tabletracker.features.order.ui.state

import app.tabletracker.features.customer.data.model.Customer
import app.tabletracker.features.inventory.data.entity.MenuItem
import app.tabletracker.features.order.data.entity.Order
import app.tabletracker.features.order.data.entity.OrderItem
import app.tabletracker.features.order.data.entity.OrderType
import app.tabletracker.features.order.data.entity.OrderWithOrderItems


sealed class OrderUiEvent {
    data class CreateNewOrder(val orderType: OrderType) : OrderUiEvent()
    data class UpdateCurrentOrder(val order: Order) : OrderUiEvent()
    data class UpdateCustomer(val customer: Customer) : OrderUiEvent()
    data class AddMenuItemToOrder(val menuItem: MenuItem) : OrderUiEvent()
    data class RemoveItemFromOrder(val orderItem: OrderItem) : OrderUiEvent()
    data class UpdateOrderItem(val orderItem: OrderItem) : OrderUiEvent()
    data class SetCurrentOrderWithOrderItems(val orderWithOrderItems: OrderWithOrderItems?) :
        OrderUiEvent()
}