package app.tabletracker.core.navigation

import kotlinx.serialization.Serializable

sealed class Screen() {
    @Serializable
    data object LoadingScreen : Screen()

    @Serializable
    data object RegisterRestaurantScreen : Screen()

    @Serializable
    data object RegisterLicenceScreen : Screen()

    @Serializable
    data object StartOrderScreen : Screen()

    @Serializable
    data object TakeOrderScreen : Screen()

    @Serializable
    data object RunningOrderScreen : Screen()

    @Serializable
    data object BookingScreen : Screen()

    @Serializable
    data object SettingsScreen : Screen()

    @Serializable
    data object InventoryScreen : Screen()
    @Serializable
    data object CategoryScreen : Screen()
    @Serializable
    data object MenuItemScreen : Screen()
}

