package app.tabletracker.util

import app.tabletracker.core.ui.SplitRatio
import app.tabletracker.features.order.data.entity.OrderType
import app.tabletracker.features.order.data.entity.PaymentMethod


object TableTrackerDefault {
    val SplitRatio = SplitRatio(leftWeight = 0.3f)
    val availablePaymentMethods =
        listOf(PaymentMethod.Cash, PaymentMethod.Card, PaymentMethod.Contactless)
    val availableOrderTypes = listOf(OrderType.DineIn, OrderType.TakeOut, OrderType.Delivery)
}