package app.tabletracker.settings.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.tabletracker.core.ui.TabbedScreen
import app.tabletracker.core.ui.component.CoreAlphabeticKeyboard
import app.tabletracker.core.ui.component.KeyboardDialog
import app.tabletracker.core.ui.component.KeyboardType
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
                Column {
                    var dialogState by rememberSaveable {
                        mutableStateOf(false)
                    }
                    var buttonText by rememberSaveable {
                        mutableStateOf("")
                    }
                    Button(onClick = { dialogState = true }) {
                        Text(text = "Show Dialog $buttonText")
                    }

                    if (dialogState) {
                        KeyboardDialog(
                            onDismissRequest = { dialogState = false },
                            value = buttonText,
                            keyboardType = KeyboardType.Numeric
                        ) {
                            buttonText = it
                            dialogState = false
                        }
                    }
                }
            }
        }
    }
}