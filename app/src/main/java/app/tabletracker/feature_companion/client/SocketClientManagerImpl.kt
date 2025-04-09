package app.tabletracker.feature_companion.client

import android.util.Log
import app.tabletracker.feature_companion.model.ServerResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import java.net.Socket

class SocketClientManagerImpl(
    private val onResponseReceived: (response: ServerResponse) -> Unit
): SocketClientManager {

    private var socket: Socket? = null
    private val clientState = MutableStateFlow(ClientState())

    override suspend fun connectToServer(ipAddress: String, port: Int) {
        try {
            socket = Socket(ipAddress, port)
            socket?.isConnected.let {
                clientState.update {
                    it.copy(
                        isConnected = true,
                        serverAddress = ipAddress
                    )
                }
                observeServer()
            }
        } catch (e: Exception) {}
    }
    private fun observeServer() {
        val reader = socket?.inputStream?.bufferedReader()
        try {
            while (clientState.value.isConnected) {
                val data = reader?.readLine() ?: break
                Log.d("SocketClientManagerImpl", "observeServer: $data")
                val response = Json.decodeFromString(ServerResponse.serializer(), data)
                Log.d("SocketClientManagerImpl", "observeServer: $response")
                onResponseReceived(response)
            }
        } catch (e: Exception) {
            disconnectFromServer()
        }
    }

    override fun disconnectFromServer() {
        socket?.close()
        socket = null
        clientState.update {
            it.copy(
                isConnected = false,
                serverAddress = null
            )
        }
    }

    override suspend fun transmitDataToServer(data: String) {
        val writer = socket?.outputStream?.bufferedWriter()
        writer?.let {
            try {
                it.write(data + "\n")
                it.flush()
            } catch (e: Exception) {
                disconnectFromServer()
            }
        }
    }

    override fun observeClientState(): StateFlow<ClientState> = clientState
}