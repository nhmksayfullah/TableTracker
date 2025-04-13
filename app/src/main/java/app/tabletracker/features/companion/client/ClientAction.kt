package app.tabletracker.features.companion.client

enum class ClientAction {
    Connect, // Will have serverAddress as parameter
    Disconnect,
    RequestRestaurantInfo,
    RequestMenu,
    RequestOrderInfo, // Will have orderId as parameter
    SendOrderInfo // Will have orderId as parameter
}