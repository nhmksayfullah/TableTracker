package app.tabletracker.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ShoppingCart

sealed class BottomNavigationOption(val navOption: NavigationOption) {
    data object Order: BottomNavigationOption(
        NavigationOption(
            id = 0,
            title = "Order",
            icon = Icons.Default.ShoppingCart,
            route = Screen.StartOrderScreen.route
        )
    )
    data object RunningOrder: BottomNavigationOption(
        NavigationOption(
            id = 1,
            title = "Running Order",
            icon = Icons.Default.Add,
            route = Screen.RunningOrderScreen.route
        )
    )
    data object Bookings: BottomNavigationOption(
        NavigationOption(
            id = 2,
            title = "Bookings",
            icon = Icons.Default.DateRange,
            route = Screen.BookingScreen.route
        )
    )
}