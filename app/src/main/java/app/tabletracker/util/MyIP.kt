package app.tabletracker.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


/**
 * Retrieves the local IP address of the device connected to a Wi-Fi or cellular network.
 *
 * This function checks for an active network connection (either Wi-Fi or cellular). If a connection
 * is found, it iterates through the link addresses associated with that network and returns the
 * first site-local IP address it encounters.
 *
 * A "site-local" address is an IP address that is only valid within a private network (e.g.,
 * 192.168.x.x, 10.x.x.x, or 172.16.x.x to 172.31.x.x).  It is commonly used for devices within a
 * home or office network.
 *
 * @param context The application context.
 * @return The local IP address as a string (e.g., "192.168.1.10") or null if:
 *         - There is no active network connection.
 *         - The active network is not Wi-Fi or cellular.
 *         - No site-local IP address is found within the active network's link addresses.
 *         - There's an error getting network or link information.
 */
fun getLocalIpAddress(context: Context): String? {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return null
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return null
    if (!capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) && !capabilities.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR
        )
    ) return null
    val linkProperties = connectivityManager.getLinkProperties(network) ?: return null
    for (address in linkProperties.linkAddresses) {
        if (address.address.isSiteLocalAddress) return address.address.hostAddress
    }
    return null
}
