package app.tabletracker.core.navigation

sealed class Applications(val route: String) {
    data object MenuManagementApp: Applications(route = "menu_management_app")
    data object OrderManagementApp: Applications(route = "order_management_app")
    data object BookingManagementApp: Applications(route = "booking_management_app")
    data object LoadingApp: Applications(route = "loading_app")
    data object AuthenticationApp: Applications(route = "authentication_app")
    data object SettingsApp: Applications(route = "settings_app")
}