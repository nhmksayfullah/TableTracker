package app.tabletracker.features.companion.model

import app.tabletracker.features.order.data.entity.OrderWithOrderItems
import kotlinx.serialization.Serializable

@Serializable
sealed class ClientRequest {
    @Serializable
    data object SetupRestaurant : ClientRequest()
    @Serializable
    data object SyncMenu : ClientRequest()
    @Serializable
    data class SyncOrder(val orderId: String) : ClientRequest()
    @Serializable
    data class IncomingOrder(val orderWithOrderItems: OrderWithOrderItems) : ClientRequest()
}