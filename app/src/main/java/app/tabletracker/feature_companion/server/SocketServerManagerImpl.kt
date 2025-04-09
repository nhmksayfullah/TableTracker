package app.tabletracker.feature_companion.server

import android.content.Context
import app.tabletracker.feature_companion.model.ClientRequest
import app.tabletracker.util.getLocalIpAddress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.ServerSocket
import java.net.Socket

class SocketServerManagerImpl(
    private val context: Context,
    private val onRequestReceived: (request: ClientRequest) -> Unit,
) : SocketServerManager {

    private var serverSocket: ServerSocket? = null
    private val clients = mutableMapOf<String, Socket>()
    private val serverState = MutableStateFlow(ServerState())

    override suspend fun startServer() {
        if (serverState.value.isRunning) return
        try {
            val ipAddress = getLocalIpAddress(context)
            if (ipAddress == null) return
            serverSocket = ServerSocket(0).apply { reuseAddress = true }
            val assignedPort = serverSocket?.localPort
            if (assignedPort == null) return

            serverState.update {
                it.copy(
                    isRunning = true,
                    ipAddress = ipAddress,
                    port = assignedPort
                )
            }


            while (serverState.value.isRunning) {
                val clientSocket = try {
                    serverSocket?.accept()
                } catch (e: Exception) {
                    null
                }
                clientSocket?.let { socket ->
                    CoroutineScope(Dispatchers.IO).launch {
                        handleClient(socket)
                    }
                }
            }
        } catch (e: Exception) {
        } finally {
            stopServer()
        }
    }

    private fun handleClient(socket: Socket) {
        val clientId = "${socket.inetAddress.hostAddress}:${socket.port}"
        clients[clientId] = socket
        serverState.update {
            it.copy(
                connectedClients = clients.keys.toList()
            )
        }
        val reader = socket.inputStream.bufferedReader()
        val writer = socket.outputStream.bufferedWriter()

        try {
            while (serverState.value.isRunning) {
                val data = try {
                    reader.readLine()
                } catch (e: Exception) {
                    null
                }
                if (data == null) break
                val request = Json.decodeFromString(ClientRequest.serializer(), data)
                onRequestReceived(request)
            }
        } catch (e: Exception) {
        } finally {
            disconnectClient(clientId)
        }
    }

    override suspend fun stopServer() {
        if (!serverState.value.isRunning) return
        try {
            clients.values.forEach { it.close() }
            clients.clear()
            serverSocket?.close()
            serverSocket = null
            serverState.update {
                it.copy(
                    isRunning = false,
                    ipAddress = null,
                    port = null,
                    connectedClients = emptyList()
                )
            }
        } catch (e: Exception) {
        }
    }

    override suspend fun transmitDataToClient(clientId: String, data: String) {
        val clientSocket = clients[clientId]
        clientSocket?.let {
            try {
                val writer = it.outputStream.bufferedWriter()
                writer.write(data + "\n")
                writer.flush()
            } catch (e: Exception) {
            }
        }
    }

    override fun disconnectClient(clientId: String) {
        clients[clientId]?.close()
        clients.remove(clientId)
        serverState.update {
            it.copy(
                connectedClients = clients.keys.toList()
            )
        }
    }

    override fun observeServerState(): StateFlow<ServerState> = serverState
}