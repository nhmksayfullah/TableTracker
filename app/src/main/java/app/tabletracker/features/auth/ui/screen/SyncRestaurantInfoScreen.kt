package app.tabletracker.features.auth.ui.screen

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.tabletracker.features.auth.ui.AuthViewModel
import app.tabletracker.features.companion.client.ClientAction
import app.tabletracker.features.companion.client.SocketClientService

@Composable
fun SyncRestaurantInfoScreen(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    onGetStarted: () -> Unit
) {
    val deviceType by authViewModel.deviceType.collectAsStateWithLifecycle()
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        modifier = modifier,
        topBar = {
            Row {
                Button(
                    onClick = {}
                ) {
                    Text(text = "${deviceType.name} Device")
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (uiState.restaurant != null && uiState.restaurantExtra != null) {
                Text(
                    text = "Welcome back ${uiState.restaurant?.name}",
                    style = MaterialTheme.typography.headlineLarge
                )
                if (uiState.hasInventory) {
                    Button(
                        onClick = onGetStarted
                    ) {
                        Text("Get Started")
                    }
                } else {
                    Button(
                        onClick = {
                            Intent(context.applicationContext, SocketClientService::class.java).also {
                                it.action = ClientAction.RequestMenu.toString()
                                context.applicationContext.startService(it)
                            }
                        }
                    ) {
                        Text(text = "Sync Menu")
                    }
                }
            } else {
                Button(
                    onClick = {
                        Intent(context.applicationContext, SocketClientService::class.java).also {
                            it.action = ClientAction.RequestRestaurantInfo.toString()
                            context.applicationContext.startService(it)
                        }
                    }
                ) {
                    Text(text = "Sync Restaurant Info")
                }
            }

        }
    }
}