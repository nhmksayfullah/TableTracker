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

// Data class to hold sync status
data class SyncStatus(
    val status: String = "",
    val message: String = "",
    val isInProgress: Boolean = false,
    val isCompleted: Boolean = false,
    val isFailed: Boolean = false
)

@Composable
fun SyncRestaurantInfoScreen(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    onGetStarted: () -> Unit
) {
    val deviceType by authViewModel.deviceType.collectAsStateWithLifecycle()
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // State for restaurant info sync status
    var restaurantInfoSyncStatus by remember { mutableStateOf(SyncStatus()) }

    // State for menu sync status
    var menuSyncStatus by remember { mutableStateOf(SyncStatus()) }

    // Animation for progress
    val restaurantInfoProgress by animateFloatAsState(
        targetValue = if (restaurantInfoSyncStatus.isCompleted) 1f 
                     else if (restaurantInfoSyncStatus.isInProgress) 0.7f else 0f,
        label = "restaurantInfoProgress"
    )

    val menuProgress by animateFloatAsState(
        targetValue = if (menuSyncStatus.isCompleted) 1f 
                     else if (menuSyncStatus.isInProgress) 0.7f else 0f,
        label = "menuProgress"
    )

    // Broadcast receivers for sync status updates
    val restaurantInfoSyncReceiver = rememberSyncReceiver { status ->
        restaurantInfoSyncStatus = status
        if (status.isCompleted) authViewModel.readRestaurantInfo()
    }

    val menuSyncReceiver = rememberSyncReceiver { status ->
        menuSyncStatus = status
    }

    // Register and unregister broadcast receivers
    DisposableEffect(Unit) {
        val restaurantInfoFilter = IntentFilter(ACTION_SYNC_RESTAURANT_INFO_STATUS)
        val menuFilter = IntentFilter(ACTION_SYNC_MENU_STATUS)

        ContextCompat.registerReceiver(
            context,
            restaurantInfoSyncReceiver,
            restaurantInfoFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        ContextCompat.registerReceiver(
            context,
            menuSyncReceiver,
            menuFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        onDispose {
            context.unregisterReceiver(restaurantInfoSyncReceiver)
            context.unregisterReceiver(menuSyncReceiver)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = true,
                    onClick = {},
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = if (deviceType.name == "Main") 
                            MaterialColor.Blue.color 
                        else MaterialColor.Red.color,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    label = {
                        Text(
                            text = "${deviceType.name} Device"
                        )
                    }
                )

                if (uiState.restaurant != null) {
                    Text(
                        text = uiState.restaurant?.name ?: "",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        },
        bottomBar = {
            // Get Started Button
            AnimatedVisibility(
                visible = uiState.restaurant != null && uiState.restaurantExtra != null && uiState.hasInventory && menuSyncStatus.isCompleted,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "All Set!",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Your restaurant information and menu have been synced successfully.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onGetStarted,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Get Started")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Text(
                text = "Sync Restaurant Information",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Welcome message with restaurant name
            AnimatedVisibility(
                visible = uiState.restaurant != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(
                    text = "Welcome ${uiState.restaurant?.name}",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Restaurant Info Card
            SyncCard(
                title = "Restaurant Information",
                description = "Sync basic restaurant information like name, address, and contact details",
                syncStatus = restaurantInfoSyncStatus,
                progress = restaurantInfoProgress,
                isEnabled = !restaurantInfoSyncStatus.isInProgress && !menuSyncStatus.isInProgress,
                onSyncClick = {
                    Intent(context.applicationContext, SocketClientService::class.java).also {
                        it.action = ClientAction.RequestRestaurantInfo.toString()
                        context.applicationContext.startService(it)
                    }
                },
                isVisible = true
            )

            // Menu Card
            SyncCard(
                title = "Menu Information",
                description = "Sync menu items, categories, and pricing information",
                syncStatus = menuSyncStatus,
                progress = menuProgress,
                isEnabled = (uiState.restaurant != null && uiState.restaurantExtra != null) && 
                           !restaurantInfoSyncStatus.isInProgress && !menuSyncStatus.isInProgress,
                onSyncClick = {
                    Intent(context.applicationContext, SocketClientService::class.java).also {
                        it.action = ClientAction.RequestMenu.toString()
                        context.applicationContext.startService(it)
                    }
                },
                isVisible = true
            )


        }
    }
}

@Composable
fun SyncCard(
    title: String,
    description: String,
    syncStatus: SyncStatus,
    progress: Float,
    isEnabled: Boolean,
    onSyncClick: () -> Unit,
    isVisible: Boolean
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Progress indicator
                if (syncStatus.isInProgress || syncStatus.isCompleted || syncStatus.isFailed) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = when {
                            syncStatus.isCompleted -> MaterialTheme.colorScheme.primary
                            syncStatus.isFailed -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.tertiary
                        },
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Status message
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        when {
                            syncStatus.isCompleted -> {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            syncStatus.isFailed -> {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                            syncStatus.isInProgress -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                            else -> {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = syncStatus.message,
                            style = MaterialTheme.typography.bodySmall,
                            color = when {
                                syncStatus.isCompleted -> MaterialTheme.colorScheme.primary
                                syncStatus.isFailed -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onSyncClick,
                    enabled = isEnabled,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = when {
                            syncStatus.isCompleted -> "Sync Again"
                            syncStatus.isInProgress -> "Syncing..."
                            else -> "Sync Now"
                        }
                    )
                }
            }
        }
    }
}

// Create a higher-order function that returns a BroadcastReceiver
@Composable
private fun rememberSyncReceiver(
    onStatusUpdate: (SyncStatus) -> Unit
) = remember {
    object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val status = intent?.getStringExtra(EXTRA_SYNC_STATUS) ?: ""
            val message = intent?.getStringExtra(EXTRA_SYNC_MESSAGE) ?: ""
            onStatusUpdate(
                SyncStatus(
                    status = status,
                    message = message,
                    isInProgress = status == SYNC_STATUS_IN_PROGRESS,
                    isCompleted = status == SYNC_STATUS_COMPLETED,
                    isFailed = status == SYNC_STATUS_FAILED
                )
            )
        }
    }
}