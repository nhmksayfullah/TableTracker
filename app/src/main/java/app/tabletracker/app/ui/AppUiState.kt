package app.tabletracker.app.ui

import app.tabletracker.core.navigation.Applications
import app.tabletracker.core.navigation.Screen

data class AppUiState(
    val isLoading: Boolean = true,
    val readyToTakeOrder: Boolean = false,
    val currentApplication: Applications = Applications.LoadingApp,
    val currentScreen: Screen = Screen.LoadingScreen,
    val isUserRegistered: Boolean = false
)
