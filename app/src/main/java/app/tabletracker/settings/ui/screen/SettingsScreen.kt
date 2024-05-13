package app.tabletracker.settings.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.tabletracker.common.data.RestaurantExtra
import app.tabletracker.core.ui.TabbedScreen
import app.tabletracker.settings.ui.SettingsUiEvent
import app.tabletracker.settings.ui.SettingsViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    onCompleteClick: () -> Unit
) {
    BackHandler(true) {}

    val settingsUiState by settingsViewModel.uiState.collectAsState()
    var totalTable by rememberSaveable {
        mutableStateOf("")
    }
    var firstTime by rememberSaveable {
        mutableStateOf(true)
    }

    Log.d("settings: ", settingsUiState.restaurantInfo.toString())
    Log.d("settings: ", settingsUiState.restaurantExtra.toString())

    val settingsTitle = listOf("General", "Printer", "Receipt")
    TabbedScreen(titles = settingsTitle) {
        when (it) {
            0 -> {
                Scaffold(
                    floatingActionButton = {
                        ExtendedFloatingActionButton(onClick = {
                            if (settingsUiState.restaurantExtra != null) {
                                val total = totalTable.toIntOrNull() ?: 0
                                settingsViewModel.onEvent(SettingsUiEvent.UpdateRestaurantExtra(
                                    settingsUiState.restaurantExtra!!.copy(totalTable = total)))
                            } else {
                                if (settingsUiState.restaurantInfo != null) {
                                    val total = totalTable.toIntOrNull() ?: 0
                                    settingsViewModel.onEvent(
                                        SettingsUiEvent.UpdateRestaurantExtra(
                                            RestaurantExtra(
                                                restaurantId = settingsUiState.restaurantInfo!!.id,
                                                totalTable = total
                                            )
                                        )
                                    )
                                }
                            }
                            Log.d("settings: ", settingsUiState.restaurantExtra.toString())
                        }) {
                            Text(text = "Update")
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                            .padding(16.dp)
                    ) {
                        TextField(
                            value = if (firstTime) settingsUiState.restaurantExtra?.totalTable?.toString() ?: totalTable else totalTable,
                            onValueChange = {
                                firstTime = false
                                totalTable = it
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            placeholder = {
                                Text(text = "13")
                            },
                            label = {
                                Text(text = "Total DineIn table")
                            },
                            singleLine = true
                        )
                    }
                }
            }
        }
    }
}