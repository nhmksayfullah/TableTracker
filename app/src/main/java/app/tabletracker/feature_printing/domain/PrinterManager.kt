package app.tabletracker.feature_printing.domain

import android.annotation.SuppressLint
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
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.usb.UsbConnection
import com.dantsu.escposprinter.connection.usb.UsbPrintersConnections


class PrinterManager(private val activity: Activity) {
    companion object {
        private const val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
    }

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
                if (ACTION_USB_PERMISSION == action) {
                    synchronized(this) {
                        val usbManager: UsbManager? =
                            activity.getSystemService(Context.USB_SERVICE) as UsbManager?

                        @Suppress("DEPRECATION")
                        val usbDevice: UsbDevice? =
                            intent.getParcelableExtra<Parcelable>(UsbManager.EXTRA_DEVICE) as UsbDevice?
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (usbManager != null && usbDevice != null) {
                                // YOUR PRINT CODE HERE
                                val printer =
                                    EscPosPrinter(
                                        UsbConnection(usbManager, usbDevice),
                                        203,
                                        80f,
                                        46
                                    )
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

    @SuppressLint("NewApi")
    private fun printUsb(usbReceiver: BroadcastReceiver) {
        val usbConnection: UsbConnection? = UsbPrintersConnections.selectFirstConnected(activity)
        val usbManager: UsbManager? = activity.getSystemService(Context.USB_SERVICE) as UsbManager?
        if (usbConnection != null && usbManager != null) {
            val permissionIntent = PendingIntent.getBroadcast(
                activity,
                0,
                Intent(ACTION_USB_PERMISSION),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
            )
            val filter = IntentFilter(ACTION_USB_PERMISSION)
            activity.registerReceiver(usbReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
            usbManager.requestPermission(usbConnection.device, permissionIntent)
        }
    }
}