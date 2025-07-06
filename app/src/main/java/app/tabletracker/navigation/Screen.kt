package app.tabletracker.navigation

import kotlinx.serialization.Serializable

sealed class Screen() {
    @Serializable
    data object LoadingScreen : Screen()

    @Serializable
    data object GetStartedScreen : Screen()

    @Serializable
    data object RegisterRestaurantScreen : Screen()

    @Serializable
    data object RegisterLicenceScreen : Screen()

    @Serializable
    data object LoginScreen : Screen()

    @Serializable
    data object ScanQrCodeScreen : Screen()

    @Serializable
    data class SyncRestaurantInfoScreen(val serverAddress: String) : Screen()

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

    @Serializable
    data object ExplorerScreen : Screen()

    @Serializable
    data class ChildExplorerScreen(val categoryId: Int) : Screen()
}
