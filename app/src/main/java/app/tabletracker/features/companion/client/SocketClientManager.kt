package app.tabletracker.features.companion.client

import kotlinx.coroutines.flow.StateFlow

interface SocketClientManager {
    suspend fun connectToServer(ipAddress: String, port: Int)
    fun disconnectFromServer()
    suspend fun transmitDataToServer(data: String)
    fun observeClientState(): StateFlow<ClientState>
}