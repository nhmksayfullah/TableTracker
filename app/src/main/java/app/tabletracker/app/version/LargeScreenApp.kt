package app.tabletracker.app.version

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.tabletracker.app.ui.AppUiEvent
import app.tabletracker.app.ui.AppUiState
import app.tabletracker.auth.authenticationApp
import app.tabletracker.core.navigation.Applications
import app.tabletracker.core.navigation.BottomNavigationOption
import app.tabletracker.core.navigation.ExtraNavOption
import app.tabletracker.core.navigation.Screen
import app.tabletracker.core.navigation.SetupBottomNavigationBar
import app.tabletracker.feature_menu.menuManagementApp
import app.tabletracker.feature_order.orderManagementApp
import app.tabletracker.settings.settingsApp

@Composable
fun LargeScreenApp(
    appUiState: AppUiState,
    onAppUiEvent: (AppUiEvent) -> Unit
) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            if (!appUiState.isLoading) {
                if (!appUiState.isUserRegistered) return@Scaffold
                if (appUiState.currentScreen == Screen.TakeOrderScreen) return@Scaffold
                SetupBottomNavigationBar(
                    navOptions = if (appUiState.currentScreen == Screen.EditMenuScreen || appUiState.currentScreen == Screen.SettingsScreen)
                        listOf()
                    else listOf(
                        BottomNavigationOption.Order,
                        BottomNavigationOption.RunningOrder
                    ),
                    extraNavOptions = if (appUiState.currentScreen == Screen.EditMenuScreen || appUiState.currentScreen == Screen.SettingsScreen)
                        listOf(ExtraNavOption.Done)
                    else listOf(
                        ExtraNavOption.Edit,
                        ExtraNavOption.Settings
                    ),
                    onNavigationItemClick = {
                        // TODO("check the null safety again")
                        if (currentDestination?.route != Screen.TakeOrderScreen.route) {
                            if (currentDestination?.route != it.navOption.route) {
                                navController.navigate(
                                    route = it.navOption.route ?: Screen.StartOrderScreen.route
                                ) {
                                    popUpTo(navController.graph.findStartDestination().id)
                                    launchSingleTop = true
                                }
                            }

                        }
                    },
                    onExtraNavOptionClick = {
                        when (it) {
                            ExtraNavOption.Done -> {
                                if (appUiState.readyToTakeOrder) {
                                    onAppUiEvent(AppUiEvent.ChangeScreen(Screen.StartOrderScreen))
                                    onAppUiEvent(AppUiEvent.ChangeApplication(Applications.OrderManagementApp))
                                    navController.popBackStack()

                                }
                            }

                            ExtraNavOption.Edit -> {
                                if (currentDestination?.route != Screen.TakeOrderScreen.route) {
                                    onAppUiEvent(AppUiEvent.ChangeScreen(Screen.EditMenuScreen))
                                    onAppUiEvent(AppUiEvent.ChangeApplication(Applications.MenuManagementApp))
                                    navController.navigate(Applications.MenuManagementApp.route)
                                }

                            }

                            ExtraNavOption.Settings -> {
                                onAppUiEvent(AppUiEvent.ChangeScreen(Screen.SettingsScreen))
                                navController.navigate(Applications.SettingsApp.route)
                            }
                        }
                    }
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Applications.LoadingApp.route,
            modifier = Modifier.padding(it),
        ) {
            composable(Applications.LoadingApp.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                if (appUiState.currentApplication == Applications.OrderManagementApp) {
                    navController.navigate(Applications.OrderManagementApp.route)
                    onAppUiEvent(AppUiEvent.ChangeScreen(Screen.StartOrderScreen))

                } else if (appUiState.currentApplication == Applications.MenuManagementApp) {
                    navController.navigate(Applications.MenuManagementApp.route)
                    onAppUiEvent(AppUiEvent.ChangeScreen(Screen.EditMenuScreen))
                } else if (appUiState.currentApplication == Applications.AuthenticationApp) {
                    navController.navigate(Applications.AuthenticationApp.route)
                    onAppUiEvent(AppUiEvent.ChangeScreen(Screen.RegisterRestaurantScreen))
                }
            }

            authenticationApp(
                navController = navController,
                onAppUiEvent = {
                    onAppUiEvent(it)
                }
            )

            menuManagementApp(
                navController = navController,
                appUiState = appUiState
            ) {
                onAppUiEvent(it)
            }

            orderManagementApp(
                navController = navController,
                onAppUiEvent = {
                    onAppUiEvent(it)
                }
            )

            settingsApp(
                navController = navController,
                onAppUiEvent = {
                    onAppUiEvent(it)
                }
            )
        }
    }
}