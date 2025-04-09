package app.tabletracker.feature_companion.server

import kotlinx.coroutines.flow.StateFlow

interface SocketServerManager {
    suspend fun startServer()
    suspend fun stopServer()
    suspend fun transmitDataToClient(clientId: String, data: String)
    fun disconnectClient(clientId: String)
    fun observeServerState(): StateFlow<ServerState>

}

const val ACTION_REQUEST_SERVER_ADDRESS = "action.REQUEST_SERVER_ADDRESS"
const val ACTION_SERVER_ADDRESS_AVAILABLE = "action.SERVER_ADDRESS_AVAILABLE"
const val EXTRA_SERVER_ADDRESS = "extra.SERVER_ADDRESS"
const val ACTION_CLIENT_CONNECTED = "action.CLIENT_CONNECTED"
