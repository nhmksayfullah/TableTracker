package app.tabletracker.app.ui

import app.tabletracker.core.navigation.Applications
import app.tabletracker.core.navigation.Screen

sealed class AppUiEvent {
    data class ChangeApplication(val application: Applications): AppUiEvent()
    data class ChangeScreen(val screen: Screen): AppUiEvent()
}