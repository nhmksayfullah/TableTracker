package app.tabletracker.feature_order.v2.state

import app.tabletracker.feature_order.data.entity.OrderType


sealed class OrderUiEvent2 {
    data class CreateNewOrder(val orderType: OrderType): OrderUiEvent2()
}