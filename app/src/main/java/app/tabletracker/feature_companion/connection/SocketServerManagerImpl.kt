package app.tabletracker.feature_companion.connection

import android.content.Context
import app.tabletracker.util.getLocalIpAddress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket


/**
 * Implementation of the [SocketServerManager] interface responsible for managing a socket server.
 *
 * This class handles the lifecycle of the server, including starting, stopping,
 * accepting client connections, handling client communication, and broadcasting messages.
 * It also manages the server's state and provides a way to observe it.
 *
 * @property context The application context, used for retrieving the local IP address.
 */
class SocketServerManagerImpl(
    private val context: Context
) : SocketServerManager {


    private var serverSocket: ServerSocket? = null

    private val clientSockets = mutableMapOf<String, Socket>()

    private val serverState = MutableStateFlow(ServerState())

    override suspend fun startServer() {
        if (serverState.value.isRunning) {
            logState("Server is already running.")
            return
        }
        try {
            val ipAddress = getLocalIpAddress(context)
            if (ipAddress == null) {
                logState("Failed to retrieve local IP address.")
                return
            }
            serverSocket = ServerSocket(0).apply {
                reuseAddress = true
            } // Bind to port 0 to use an available port

            val assignedPort = serverSocket?.localPort // Retrieve the assigned port
            if (assignedPort == null) {
                logState("Failed to retrieve assigned port.")
                return
            }

            // Update the server state with the assigned port and IP address
            serverState.update {
                it.copy(isRunning = true, port = assignedPort, ipAddress = ipAddress)
            }
            logState("Server started on port $assignedPort with IP address $ipAddress")

            while (serverState.value.isRunning) {
                val clientSocket = try {
                    serverSocket?.accept() // Accept a new client connection
                } catch (e: IOException) {
                    logState("Error accepting client connection: ${e.message}")
                    null
                }

                clientSocket?.let { socket ->
                    CoroutineScope(Dispatchers.IO).launch {
                        handleClient(socket) // Launch a coroutine to handle this client
                    }
                }
            }
        } catch (e: Exception) {
            when (e) {
                is IOException -> logState("IO error while starting server: ${e.message}")
                else -> logState("Error while starting server: ${e.message}")
            }
        } finally {
            stopServer() // Ensure resources are cleaned up
        }
    }


    private fun handleClient(socket: Socket) {
        val clientId = "${socket.inetAddress.hostAddress}:${socket.port}"
        clientSockets[clientId] = socket // Add the client to the active connections map
        serverState.update {
            it.copy(connectedClients = clientSockets.keys.toList())
        }
        logState("Client connected: $clientId")

        val reader = socket.getInputStream().bufferedReader()
        val writer = socket.getOutputStream().bufferedWriter()

        try {
            while (serverState.value.isRunning) {
                val message = try {
                    reader.readLine() // Read the incoming message from the client
                } catch (e: IOException) {
                    logState("Error reading from client $clientId: ${e.message}")
                    null
                }

                if (message == null) break // Client disconnected

                logState("Received from $clientId: $message")

                // Update server state with the received message
                serverState.update {
                    it.copy(messages = it.messages + Message(clientId, message))
                }

                // Echo the message back to the client (optional)
                try {
                    writer.write("Echo: $message\n")
                    writer.flush()
                } catch (e: IOException) {
                    logState("Error writing to client $clientId: ${e.message}")
                }
            }
        } finally {
            disconnectClient(clientId) // Clean up when the client disconnects
        }
    }


    override suspend fun stopServer() {
        if (!serverState.value.isRunning) {
            logState("Server is not running.")
            return
        }
        try {
            clientSockets.values.forEach { it.close() }
            clientSockets.clear()
            serverSocket?.close()
            serverSocket = null
            serverState.update {
                it.copy(
                    isRunning = false,
                    port = null,
                    ipAddress = null,
                    connectedClients = emptyList()
                )
            }
            logState("Server stopped.")
        } catch (e: Exception) {
            when (e) {
                is IOException -> logState("IO error while stopping server: ${e.message}")
                else -> logState("Error while stopping server: ${e.message}")
            }
        }
    }

    override suspend fun <T> sendToClient(clientId: String, data: T) {
        val clientSocket = clientSockets[clientId]
        clientSocket?.let {
            try {
                val writer = it.getOutputStream().bufferedWriter()
                writer.write(data.toString() + "\n")
                writer.flush()
                logState("Sent to $clientId: $data")
            } catch (e: Exception) {
                when (e) {
                    is IOException -> logState("IO error while sending to client $clientId: ${e.message}")
                    else -> logState("Error while sending to client $clientId: ${e.message}")
                }
            }
        } ?: logState("Client $clientId not found.")
    }

    override suspend fun <T> broadcast(data: T) {
        clientSockets.keys.forEach { sendToClient(it, data) }
    }

    override fun observeServerState(): StateFlow<ServerState> = serverState

    override fun disconnectClient(clientId: String) {
        clientSockets[clientId]?.close()
        clientSockets.remove(clientId)
        serverState.update {
            it.copy(connectedClients = clientSockets.keys.toList())
        }
        logState("Client $clientId disconnected.")
    }


    private fun logState(message: String) {
        serverState.update {
            it.copy(log = it.log + message)
        }
    }
}
