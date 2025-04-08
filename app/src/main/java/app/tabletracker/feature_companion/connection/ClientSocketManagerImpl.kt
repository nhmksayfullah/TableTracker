package app.tabletracker.feature_companion.connection

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.Socket

/**
 * Implementation of [ClientSocketManager] using Kotlin Coroutines.
 */
class ClientSocketManagerImpl : ClientSocketManager {


    private var socket: Socket? = null

    private var heartbeatJob: Job? = null

    private val clientState = MutableStateFlow(ClientState())


    override suspend fun connectToServer(ip: String, port: Int) {
        try {
            socket = Socket(ip, port)
            socket?.isConnected.let {
                clientState.update {
                    it.copy(
                        isConnected = true,
                        serverAddress = "$ip:$port"
                    )
                }
                logState("Connected to server at $ip:$port")
                readFromServer()
            }

        } catch (e: Exception) {
            when (e) {
                is IOException -> logState("IO error while connecting to server: ${e.message}")
                else -> logState("Error while connecting to server: ${e.message}")
            }
        } finally {
            disconnect()
        }
    }

    private fun readFromServer() {
        val reader = socket?.getInputStream()?.bufferedReader()
        try {
            while (clientState.value.isConnected) {
                val line = reader?.readLine() ?: break
                logState("Received from server: $line")
                val message = Message("SERVER", line)
                clientState.update {
                    it.copy(messages = it.messages + message)
                }
            }
        } catch (e: Exception) {
            when (e) {
                is IOException -> logState("IO error while reading from server: ${e.message}")
                else -> logState("Error while reading from server: ${e.message}")
            }
            disconnect()
        }
    }

    override suspend fun <T> sendToServer(data: T) {
        val writer = socket?.getOutputStream()?.bufferedWriter()
        writer?.let {
            try {
                it.write(data.toString() + "\n")
                it.flush()
                logState("Sent to server: $data")
                val message = Message("CLIENT", data)
                clientState.update {
                    it.copy(messages = it.messages + message)
                }
            } catch (e: Exception) {
                when (e) {
                    is IOException -> logState("IO error while sending to server: ${e.message}")
                    else -> logState("Error while sending to server: ${e.message}")
                }
                disconnect()
            }
        }
    }


    override fun observeClientState(): StateFlow<ClientState> = clientState


    override fun disconnect() {
        heartbeatJob?.cancel()
        socket?.close()
        socket = null
        clientState.update {
            it.copy(isConnected = false, serverAddress = null)
        }
        logState("Disconnected from server")
    }

    override suspend fun startHeartbeat(intervalMillis: Long) {
        if (heartbeatJob?.isActive == true) {
            return
        }
        heartbeatJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                sendToServer("Heartbeat")
                logState("Heartbeat sent")
                delay(intervalMillis)
            }
        }
    }

    override fun stopHeartbeat() {
        if (heartbeatJob?.isActive == true) {
            heartbeatJob?.cancel()
            logState("Heartbeat stopped")
        } else {
            logState("Heartbeat is not running")
        }
    }

    private fun logState(message: String) {
        clientState.update {
            it.copy(log = it.log + message)
        }
    }
}
