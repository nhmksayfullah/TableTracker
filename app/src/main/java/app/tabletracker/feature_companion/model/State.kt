package app.tabletracker.feature_companion.model

/**
 * Represents the current state of a server.
 *
 * This data class holds information about the server's operational status,
 * network details, connected clients, and recent activity logs and messages.
 *
 * @property isRunning Indicates whether the server is currently running. Defaults to `false`.
 * @property port The port number on which the server is listening. `null` if the server is not running or the port is not yet assigned.
 * @property ipAddress The IP address the server is bound to. `null` if the server is not running or if the IP address is not yet resolved. This might be the local address or public depending on server configuration.
 * @property connectedClients A list of unique identifiers representing clients currently connected to the server. Defaults to an empty list. Could be an IP, username or other unique identifier.
 * @property log A list of log entries representing recent server events. Defaults to an empty list. Each String should represent a logged entry.
 * @property messages A list of [Message] objects representing messages exchanged through the server. Defaults to an empty list. Each Message can have a type associated with it.
 *
 * @see Message
 */
data class ServerState(
    val isRunning: Boolean = false,
    val port: Int? = null,
    val ipAddress: String? = null,
    val connectedClients: List<String> = emptyList(),
    val log: List<String> = emptyList(),
    val messages: List<Message<*>> = emptyList()
)

/**
 * Represents the current state of a client in a client-server application.
 *
 * This data class encapsulates various aspects of the client's state, including
 * its connection status, server address, log messages, and received messages.
 *
 * @property isConnected A boolean indicating whether the client is currently connected to a server.
 *                       Defaults to `false`.
 * @property serverAddress An optional string representing the address of the server the client is (or was) connected to.
 *                         `null` if the client is not connected or the server address is unknown.
 * @property log A list of strings representing log messages related to the client's operations.
 *              This can include connection attempts, errors, and other relevant information.
 *              Defaults to an empty list.
 * @property messages A list of [Message] objects that have been received by the client.
 *                    Each [Message] represents a piece of data sent from the server.
 *                    Defaults to an empty list.
 */
data class ClientState(
    val isConnected: Boolean = false,
    val serverAddress: String? = null,
    val log: List<String> = emptyList(),
    val messages: List<Message<*>> = emptyList()
)

/**
 * Represents a generic message with an identifier and associated data.
 *
 * This class encapsulates a message, providing a unique identifier (`id`) and
 * the corresponding data (`data`) associated with it. The data can be of any type,
 * making this a versatile structure for various messaging scenarios.
 *
 * @param T The type of the data contained within the message.
 * @property id A unique identifier for the message. It is of type String.
 * @property data The data associated with the message. Its type is defined by the generic type parameter `T`.
 */
data class Message<T>(
    val id: String,
    val data: T
)