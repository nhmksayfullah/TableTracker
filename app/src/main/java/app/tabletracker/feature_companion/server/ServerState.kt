package app.tabletracker.feature_companion.server

data class ServerState(
    val isRunning: Boolean = false,
    val ipAddress: String? = null,
    val port: Int? = null,
    val connectedClients: List<String> = emptyList(),
)
