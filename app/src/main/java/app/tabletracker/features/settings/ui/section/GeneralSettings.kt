package app.tabletracker.features.settings.ui.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.tabletracker.core.model.RestaurantExtra
import app.tabletracker.features.settings.ui.SettingsUiEvent
import app.tabletracker.features.settings.ui.SettingsUiState

@Composable
fun GeneralSettingsSection(
    modifier: Modifier = Modifier,
    settingsUiState: SettingsUiState,
    onSettingsUiEvent: (SettingsUiEvent) -> Unit
) {
    var totalTable by rememberSaveable {
        mutableStateOf("")
    }
    var firstTime by rememberSaveable {
        mutableStateOf(true)
    }
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                if (settingsUiState.restaurantExtra != null) {
                    val total = totalTable.toIntOrNull() ?: 0
                    onSettingsUiEvent(
                        SettingsUiEvent.UpdateRestaurantExtra(
                            settingsUiState.restaurantExtra.copy(totalTable = total)
                        )
                    )
                } else {
                    if (settingsUiState.restaurantInfo != null) {
                        val total = totalTable.toIntOrNull() ?: 0
                        onSettingsUiEvent(
                            SettingsUiEvent.UpdateRestaurantExtra(
                                RestaurantExtra(
                                    restaurantId = settingsUiState.restaurantInfo.id,
                                    totalTable = total
                                )
                            )
                        )
                    }
                }
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
                value = if (firstTime) settingsUiState.restaurantExtra?.totalTable?.toString()
                    ?: totalTable else totalTable,
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