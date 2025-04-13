package app.tabletracker.features.order.ui.screen.startorder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import app.tabletracker.R
import app.tabletracker.features.companion.model.ACTION_CLIENT_CONNECTED
import app.tabletracker.features.companion.model.ACTION_REQUEST_SERVER_ADDRESS
import app.tabletracker.features.companion.model.ACTION_SERVER_ADDRESS_AVAILABLE
import app.tabletracker.features.companion.model.EXTRA_SERVER_ADDRESS
import app.tabletracker.features.companion.server.ServerAction
import app.tabletracker.features.companion.server.SocketServerService
import app.tabletracker.theme.MaterialColor
import qrgenerator.qrkitpainter.rememberQrKitPainter

@Composable
fun CompanionSection(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    var qrCodeDialogVisible by remember { mutableStateOf(false) }
    var serverAddress by remember { mutableStateOf("") }

    val receiver = remember {
        object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                val address = intent?.getStringExtra(EXTRA_SERVER_ADDRESS)
                address?.let {
                    serverAddress = it
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        val requestIntent = Intent(ACTION_REQUEST_SERVER_ADDRESS)
        context.sendBroadcast(requestIntent)
    }

    DisposableEffect(Unit) {
        val filter = IntentFilter(ACTION_SERVER_ADDRESS_AVAILABLE)
        ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        Intent(ACTION_SERVER_ADDRESS_AVAILABLE).also {
            it.setPackage(context.packageName)
            context.sendBroadcast(it)
        }

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    val clientConnectedReceiver = remember {
        object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                qrCodeDialogVisible = false
            }
        }
    }
    DisposableEffect(Unit) {
        val filter = IntentFilter(ACTION_CLIENT_CONNECTED)
        ContextCompat.registerReceiver(
            context,
            clientConnectedReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        onDispose {
            context.unregisterReceiver(clientConnectedReceiver)
        }
    }

    if (serverAddress.isNotEmpty()) {
        IconButton(
            onClick = {
                qrCodeDialogVisible = true
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_qr_code_24),
                contentDescription = "Show QR Code"
            )
        }
    }
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = if (serverAddress.isNotEmpty())
                MaterialColor.Red.color
            else MaterialColor.Blue.color
        ),
        onClick = {
            if (serverAddress.isNotEmpty()) {
                Intent(context.applicationContext, SocketServerService::class.java).also {
                    it.action = ServerAction.Stop.toString()
                    context.applicationContext.startService(it)
                }
                serverAddress = ""
                qrCodeDialogVisible = false
            } else {
                Intent(context.applicationContext, SocketServerService::class.java).also {
                    it.action = ServerAction.Start.toString()
                    context.applicationContext.startService(it)
                }
                qrCodeDialogVisible = true
            }
        }
    ) {
        Text(
            text = if (serverAddress.isNotEmpty()) "Disconnect" else "Connect with Companion"
        )
    }

    if (qrCodeDialogVisible) {
        Dialog(
            onDismissRequest = { qrCodeDialogVisible = false },
        ) {
            val painter = rememberQrKitPainter(data = serverAddress)
            Surface(
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Scan from a Companion Device"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(serverAddress)
                }
            }
        }
    }

}