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
import app.tabletracker.settings.ui.section.GeneralSettingsSection

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    onCompleteClick: () -> Unit
) {
    BackHandler(true) {}

    val settingsUiState by settingsViewModel.uiState.collectAsState()



    val settingsTitle = listOf("General", "Printer", "Receipt", "Discounts")
    TabbedScreen(titles = settingsTitle) {
        when (it) {
            0 -> {
                GeneralSettingsSection(
                    settingsUiState = settingsUiState
                ) {
                    settingsViewModel.onEvent(it)
                }
            }
            3 -> {

            }
        }
    }
}