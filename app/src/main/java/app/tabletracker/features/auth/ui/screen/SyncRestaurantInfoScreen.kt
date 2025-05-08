package app.tabletracker.features.auth.ui.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import app.tabletracker.theme.MaterialColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.tabletracker.features.auth.data.model.DeviceType
import app.tabletracker.features.auth.ui.AuthViewModel
import app.tabletracker.features.companion.client.ClientAction
import app.tabletracker.features.companion.client.SocketClientService
import app.tabletracker.features.companion.model.ACTION_SYNC_MENU_STATUS
import app.tabletracker.features.companion.model.ACTION_SYNC_RESTAURANT_INFO_STATUS
import app.tabletracker.features.companion.model.EXTRA_SYNC_MESSAGE
import app.tabletracker.features.companion.model.EXTRA_SYNC_STATUS
import app.tabletracker.features.companion.model.SYNC_STATUS_COMPLETED
import app.tabletracker.features.companion.model.SYNC_STATUS_FAILED
import app.tabletracker.features.companion.model.SYNC_STATUS_IN_PROGRESS


@Composable
fun SyncRestaurantInfoScreen(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    onGetStarted: () -> Unit
) {
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                FilterChip(
                    selected = true,
                    onClick = {},
                    label = {
                        Text("Connect as Companion Device")
                    }
                )
            }
        },
        bottomBar = {
            Column {
                Button(
                    enabled = uiState.hasInventory,
                    onClick = onGetStarted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Get Started")
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                enabled = uiState.restaurant == null && uiState.restaurantExtra == null,
                onClick = {
                    authViewModel.syncRestaurantInfo()
                }
            ) {
                Text("Sync Restaurant Info")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                enabled = !uiState.hasInventory && uiState.restaurant != null,
                onClick = {
                    authViewModel.syncMenu()
                }
            ) {
                Text("Sync Restaurant Info")
            }

        }
    }



}
