package app.tabletracker.features.auth.ui.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import app.tabletracker.features.auth.data.model.DeviceType
import app.tabletracker.features.auth.ui.AuthViewModel
import app.tabletracker.features.companion.client.ClientAction
import app.tabletracker.features.companion.client.SocketClientService
import app.tabletracker.features.companion.model.ACTION_REQUEST_SERVER_CONNECTION
import app.tabletracker.features.companion.model.ACTION_SERVER_CONNECTION_AVAILABLE
import app.tabletracker.features.companion.model.EXTRA_SERVER_ADDRESS
import app.tabletracker.features.companion.model.EXTRA_SERVER_CONNECTION
import qrscanner.CameraLens
import qrscanner.QrScanner

@Composable
fun ScanQrCodeScreen(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    onScanQrCode: (data: String) -> Unit
) {

    val context = LocalContext.current
    var serverAddress by remember {
        mutableStateOf("")
    }
    var isConnected: Boolean? by remember {
        mutableStateOf(false)
    }

    val serverConnectedReceiver = remember {
        object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                isConnected = intent?.getBooleanExtra(EXTRA_SERVER_CONNECTION, false)
                if (isConnected == true) {
                    authViewModel.updateDeviceType(DeviceType.Companion)
                    onScanQrCode(serverAddress)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        val requestIntent = Intent(ACTION_REQUEST_SERVER_CONNECTION)
        context.sendBroadcast(requestIntent)
    }
    DisposableEffect(Unit) {
        val filter = IntentFilter(ACTION_SERVER_CONNECTION_AVAILABLE)
        ContextCompat.registerReceiver(
            context,
            serverConnectedReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        Intent(ACTION_SERVER_CONNECTION_AVAILABLE).also {
            it.setPackage(context.packageName)
            context.sendBroadcast(it)
        }
        onDispose {
            context.unregisterReceiver(serverConnectedReceiver)
        }
    }
    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .weight(.3f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Scan QR Code",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(Modifier.height(16.dp))
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth(.3f)
                    .weight(.4f),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
            ) {
                QrScanner(
                    modifier = Modifier,
                    flashlightOn = false,
                    cameraLens = CameraLens.Back,
                    openImagePicker = false,
                    imagePickerHandler = {},
                    onCompletion = {
                        serverAddress = it
                        // Automatically connect when QR code is scanned
                        Intent(context.applicationContext, SocketClientService::class.java).also { intent ->
                            intent.action = ClientAction.Connect.toString()
                            intent.putExtra(EXTRA_SERVER_ADDRESS, it)
                            context.applicationContext.startService(intent)
                        }
                    },
                    onFailure = {
                        onScanQrCode(it)
                    },
                    customOverlay = {}
                )
            }
            Column(
                modifier = Modifier
                    .weight(.3f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(
                    modifier = Modifier
                        .height(32.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = serverAddress,
                        onValueChange = {
                            serverAddress = it
                        },
                        label = {
                            Text("Address")
                        },
                        placeholder = {
                            Text("123.456.7.89:0000")
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            Intent(context.applicationContext, SocketClientService::class.java).also {
                                it.action = ClientAction.Connect.toString()
                                it.putExtra(EXTRA_SERVER_ADDRESS, serverAddress)
                                context.applicationContext.startService(it)
                            }
                        }
                    ) {
                        Text("Connect")
                    }
                }
            }
        }
    }

}
