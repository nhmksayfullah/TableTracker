package app.tabletracker.feature_companion.connection

import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for managing the client-side socket connection and communication.
 *
 * This interface provides methods for connecting to a server, sending and receiving data,
 * managing the connection state, and handling heartbeat signals.
 */
interface ClientSocketManager {

    /**
     * Connects to a server at the specified IP address and port.
     */
    suspend fun connectToServer(ip: String, port: Int)

    /**
     * Disconnects from the server.
     */
    fun disconnect()

    /**
     * Sends data to the server.
     *
     * @param data The data to be sent to the server.
     */
    suspend fun <T> sendToServer(data: T)

    /**
     * Observes the client's state.
     *
     * @return A [StateFlow] emitting the current client state.
     */
    fun observeClientState(): StateFlow<ClientState>

    /**
     * Starts sending heartbeat signals to the server.
     *
     * @param intervalMillis The interval in milliseconds between heartbeat signals.
     */
    suspend fun startHeartbeat(intervalMillis: Long = 5000)

    /**
     * Stops sending heartbeat signals to the server.
     */
    fun stopHeartbeat()
}