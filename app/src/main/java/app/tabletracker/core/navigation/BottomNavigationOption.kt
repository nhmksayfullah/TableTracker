package app.tabletracker.core.navigation

import app.tabletracker.R

data class NavigationOption(
    val id: Int,
    val title: String,
    val icon: Int,
    val route: Screen
)


sealed class BottomNavigationOption(val navOption: NavigationOption) {
    data object Order: BottomNavigationOption(
        NavigationOption(
            id = 0,
            title = "Dashboard",
            icon = R.drawable.rounded_grid_view_24,
            route = Screen.StartOrderScreen
        )
    )
    data object RunningOrder: BottomNavigationOption(
        NavigationOption(
            id = 1,
            title = "Running Order",
            icon = R.drawable.outline_deployed_code_history_24,
            route = Screen.RunningOrderScreen
        )
    )
    data object Bookings: BottomNavigationOption(
        NavigationOption(
            id = 2,
            title = "Bookings",
            icon = R.drawable.outline_calendar_month_24,
            route = Screen.BookingScreen
        )
    )
    data object Inventory: BottomNavigationOption(
        NavigationOption(
            id = 3,
            title = "Inventory",
            icon = R.drawable.outline_inventory_2_24,
            route = Screen.InventoryScreen
        )
    )
    data object Settings: BottomNavigationOption(
        NavigationOption(
            id = 4,
            title = "Settings",
            icon = R.drawable.round_settings_24,
            route = Screen.SettingsScreen
        )
    )
}
fun getBottomNavigationOptions(): List<BottomNavigationOption> {
    return listOf(
        BottomNavigationOption.Order,
        BottomNavigationOption.RunningOrder,
//        BottomNavigationOption.Bookings,
        BottomNavigationOption.Inventory,
        BottomNavigationOption.Settings
    )
}