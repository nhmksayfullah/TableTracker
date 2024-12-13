package app.tabletracker.feature_order.v2.state

import app.tabletracker.feature_customer.data.model.Customer
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_order.data.entity.Order
import app.tabletracker.feature_order.data.entity.OrderItem
import app.tabletracker.feature_order.data.entity.OrderType
import app.tabletracker.feature_order.data.entity.OrderWithOrderItems


sealed class OrderUiEvent2 {
    data class CreateNewOrder(val orderType: OrderType): OrderUiEvent2()
    data class UpdateCurrentOrder(val order: Order): OrderUiEvent2()
    data class UpdateCustomer(val customer: Customer): OrderUiEvent2()
    data class AddMenuItemToOrder(val menuItem: MenuItem): OrderUiEvent2()
    data class RemoveItemFromOrder(val orderItem: OrderItem): OrderUiEvent2()
    data class UpdateOrderItem(val orderItem: OrderItem): OrderUiEvent2()
    data class SetCurrentOrderWithOrderItems(val orderWithOrderItems: OrderWithOrderItems?): OrderUiEvent2()
}