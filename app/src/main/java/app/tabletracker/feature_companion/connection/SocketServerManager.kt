package app.tabletracker.feature_companion.connection

import kotlinx.coroutines.flow.StateFlow

/**
 * `SocketServerManager` interface defines the contract for managing a socket server.
 * It provides methods to start, stop, send data to clients, broadcast data,
 * observe the server's state, and disconnect specific clients.
 */
interface SocketServerManager {

    /**
     * Starts the server on a random available port.
     */
    suspend fun startServer()

    /**
     * Stops the server and disconnects all connected clients.
     */
    suspend fun stopServer()

    /**
     * Sends data to a specific client identified by its clientId.
     * @param clientId The unique identifier of the client.
     * @param data The data to be sent to the client.
     */
    suspend fun <T> sendToClient(clientId: String, data: T)

    /**
     * Broadcasts data to all connected clients.
     */
    suspend fun <T> broadcast(data: T)

    /**
     * Observes the server's state.
     * @return A [StateFlow] emitting the current server state.
     */
    fun observeServerState(): StateFlow<ServerState>

    /**
     * Disconnects a specific client identified by its clientId.
     * @param clientId The unique identifier of the client.
     */
    fun disconnectClient(clientId: String)
}