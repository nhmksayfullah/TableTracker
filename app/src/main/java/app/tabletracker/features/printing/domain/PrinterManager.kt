package app.tabletracker.features.printing.domain

import android.content.Context

class PrinterManager(
    context: Context
) {
    private val bluetoothPrinterManager = BluetoothPrinterManager(context)
    private val usbPrinterManager = UsbPrinterManager(context)

    fun print(formattedText: String) {
        try {
            // First try to print with Bluetooth printer
            if (bluetoothPrinterManager.isBluetoothPrinterConnected()) {
                try {
                    bluetoothPrinterManager.print(formattedText)
                    return // If successful, we're done
                } catch (e: Exception) {
                }
            }

            // Fallback to normal printer
            usbPrinterManager.print(formattedText)
        } catch (e: Exception) {
        }

    }

}