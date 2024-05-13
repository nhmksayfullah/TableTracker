package app.tabletracker.core.navigation

sealed class Screen(val route: String) {
    data object StartOrderScreen: Screen(route = "start_order_screen")
    data object TakeOrderScreen: Screen(route = "take_order_screen")
    data object RunningOrderScreen: Screen(route = "running_order_screen")
    data object BookingScreen: Screen(route = "booking_screen")
    data object SettingsScreen: Screen(route = "settings_screen")
    data object EditMenuScreen: Screen(route = "edit_menu_screen")
    data object LoadingScreen: Screen(route = "loading_screen")
    data object RegisterRestaurantScreen: Screen(route = "register_restaurant_screen")
    data object RegisterLicenceScreen: Screen(route = "register_licence_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}