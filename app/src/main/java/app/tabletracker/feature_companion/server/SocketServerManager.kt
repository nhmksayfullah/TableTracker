package app.tabletracker.feature_companion.server

import kotlinx.coroutines.flow.StateFlow

interface SocketServerManager {
    suspend fun startServer()
    suspend fun stopServer()
    suspend fun transmitDataToClient(clientId: String, data: String)
    fun disconnectClient(clientId: String)
    fun observeServerState(): StateFlow<ServerState>

}


