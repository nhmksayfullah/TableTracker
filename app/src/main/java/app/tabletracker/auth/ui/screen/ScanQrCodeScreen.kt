package app.tabletracker.auth.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import qrscanner.CameraLens
import qrscanner.QrScanner

@Composable
fun ScanQrCodeScreen(
    modifier: Modifier = Modifier,
    onScanQrCode: (data: String) -> Unit
) {

    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            QrScanner(
                modifier = Modifier
                    .fillMaxSize(),
                flashlightOn = false,
                cameraLens = CameraLens.Back,
                openImagePicker = false,
                imagePickerHandler = {},
                onCompletion = {
                    onScanQrCode(it)
                },
                onFailure = {
                    onScanQrCode(it)
                }
            )
        }
    }

}