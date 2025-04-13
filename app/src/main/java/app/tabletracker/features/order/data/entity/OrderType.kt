package app.tabletracker.features.order.data.entity

enum class OrderType(val label: String) {
    DineIn("Dine In"), TakeOut("Collection"), Delivery("Delivery")
}