package app.tabletracker.feature_order.data.entity

enum class OrderType(val label: String) {
    DineIn("Dine In"), TakeOut("Collection"), Delivery("Delivery")
}