package app.tabletracker.auth.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import qrscanner.CameraLens
import qrscanner.QrScanner

@Composable
fun ScanQrCodeScreen(
    modifier: Modifier = Modifier,
    onScanQrCode: (data: String) -> Unit
) {

    var serverAddress by remember {
        mutableStateOf("")
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
                            onScanQrCode(serverAddress)
                        }
                    ) {
                        Text("Connect")
                    }
                }
            }
        }
    }

}