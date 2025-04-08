package app.tabletracker.feature_companion.model

import kotlinx.serialization.Serializable

@Serializable
sealed class ClientRequest {
    @Serializable data object SetupRestaurant : ClientRequest()
    @Serializable data object SyncMenu : ClientRequest()
    @Serializable data class SyncOrder(val orderId: String) : ClientRequest()
    @Serializable data class IncomingOrder(val orderId: String, val isNewOrder: Boolean) : ClientRequest()
}