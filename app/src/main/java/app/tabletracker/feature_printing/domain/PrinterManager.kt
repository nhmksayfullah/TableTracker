package app.tabletracker.feature_printing.domain

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.usb.UsbConnection
import com.dantsu.escposprinter.connection.usb.UsbPrintersConnections




class PrinterManager(private val activity: Activity) {
    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"

    fun print(formattedText: String) {
        val usbReceiver = createUsbReceiver(formattedText)
        printUsb(usbReceiver)
    }

    private fun createUsbReceiver(
        formattedText: String
    ): BroadcastReceiver {
        val usbReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action: String? = intent.action
                Log.d("action: ", action.toString())
                if (ACTION_USB_PERMISSION.equals(action)) {
                    synchronized(this) {
                        val usbManager: UsbManager? =
                            activity.getSystemService(Context.USB_SERVICE) as UsbManager?
                        val usbDevice: UsbDevice? =
                            intent.getParcelableExtra<Parcelable>(UsbManager.EXTRA_DEVICE) as UsbDevice?
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (usbManager != null && usbDevice != null) {
                                // YOUR PRINT CODE HERE
                                val printer =
                                    EscPosPrinter(UsbConnection(usbManager, usbDevice), 203, 80f, 46)
                                printer
                                    .printFormattedTextAndCut(formattedText)
                                printer.disconnectPrinter()

                                clearAbortBroadcast()
                                abortBroadcast()
                                activity.unregisterReceiver(this)
                            }
                        }
                    }
                }
            }
        }
        return usbReceiver
    }

    private fun printUsb(usbReceiver: BroadcastReceiver) {
        val usbConnection: UsbConnection? = UsbPrintersConnections.selectFirstConnected(activity)
        val usbManager: UsbManager? = activity.getSystemService(ComponentActivity.USB_SERVICE) as UsbManager?
        if (usbConnection != null && usbManager != null) {
            val permissionIntent = PendingIntent.getBroadcast(
                activity,
                0,
                Intent(ACTION_USB_PERMISSION),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
            )
            val filter: IntentFilter = IntentFilter(ACTION_USB_PERMISSION)
            activity.registerReceiver(usbReceiver, filter)
            usbManager.requestPermission(usbConnection.device, permissionIntent)
        }
    }
}


val dummyReceipt = """
        [C]<u><font size='big'>ORDER N°045</font></u>
        [L]
        [C]================================
        [L]
        [L]<b>BEAUTIFUL SHIRT</b>[R]9.99e
        [L]  + Size : S
        [L]
        [L]<b>AWESOME HAT</b>[R]24.99e
        [L]  + Size : 57/58
        [L]
        [C]--------------------------------
        [R]TOTAL PRICE :[R]34.98e
        [R]TAX :[R]4.23e
        [L]
        [C]================================
        [L]
        [L]<font size='tall'>Customer :</font>
        [L]Raymond DUPONT
        [L]5 rue des girafes
        [L]31547 PERPETES
        [L]Tel : +33801201456
        [L]
        [C]<barcode type='ean13' height='10'>831254784551</barcode>
        [C]<qrcode size='20'>https://dantsu.com/</qrcode>
        """.trimIndent()