package app.tabletracker.feature_companion.client

data class ClientState(
    val isConnected: Boolean = false,
    val serverAddress: String? = null,
)
