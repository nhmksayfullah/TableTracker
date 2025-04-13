package app.tabletracker.features.companion.client

data class ClientState(
    val isConnected: Boolean = false,
    val serverAddress: String? = null,
)
