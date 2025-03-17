package app.tabletracker.settings.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.tabletracker.core.ui.TabbedScreen
import app.tabletracker.core.ui.component.KeyboardDialog
import app.tabletracker.core.ui.component.DialogKeyboardType
import app.tabletracker.core.ui.component.FoodBlockComponent
import app.tabletracker.core.ui.component.SearchBarDialog
import app.tabletracker.settings.ui.SettingsViewModel
import app.tabletracker.settings.ui.section.GeneralSettingsSection
import app.tabletracker.theme.MaterialColor

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
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(120.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(MaterialColor.entries) {
                            FoodBlockComponent(
                                text = it.name,
                                containerColor = it.color,
                                modifier = Modifier
                                    .padding(4.dp)
                            )
                        }
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(120.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(MaterialColor.entries) {
                            Card(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(8.dp),
                                colors = CardDefaults
                                    .cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    ),
                            ) {
                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp)
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(4.dp)
                                            .background(it.color, RoundedCornerShape(8.dp))
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = it.name,
                                        maxLines = 2,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}