package app.tabletracker.features.printing.domain

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BluetoothPrinterManager(
    private val context: Context
) {
    private fun hasBluetoothPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        } else {
            context.checkSelfPermission(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun isBluetoothPrinterConnected(): Boolean {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        return bluetoothAdapter?.isEnabled == true && hasBluetoothPermissions()
    }

    fun print(formattedText: String) {
        // Run in background using coroutines
        CoroutineScope(Dispatchers.IO).launch {
            if (!hasBluetoothPermissions()) {
                throw SecurityException("Bluetooth permissions are not granted")
            }
            synchronized(this@BluetoothPrinterManager) {
                val printer = EscPosPrinter(
                    BluetoothPrintersConnections.selectFirstPaired(),
                    203,
                    80f,
                    46
                )
                printer.printFormattedTextAndCut(formattedText)
                printer.disconnectPrinter()
            }
        }
    }

}