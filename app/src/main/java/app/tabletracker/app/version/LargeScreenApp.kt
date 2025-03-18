package app.tabletracker.app.version

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.tabletracker.app.ui.AppUiState
import app.tabletracker.auth.authenticationNavGraph
import app.tabletracker.core.navigation.Screen
import app.tabletracker.core.navigation.SetupBottomNavigationBar
import app.tabletracker.feature_menu.InventoryApp
import app.tabletracker.feature_menu.inventoryNavGraph
import app.tabletracker.feature_order.OrderManagementApp
import app.tabletracker.feature_order.orderManagementNavGraph
import app.tabletracker.settings.settingsNavGraph

@Composable
fun LargeScreenApp(
    appUiState: AppUiState,
) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            SetupBottomNavigationBar(
                onNavigationItemClick = {
                    navController.navigate(it.navOption.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.LoadingScreen,
            modifier = Modifier.padding(it)
        ) {
            composable<Screen.LoadingScreen> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                LaunchedEffect(
                    appUiState.loading
                ) {
                    if (appUiState.loading == false) {
                        if (appUiState.hasInventory) {
                            navController.navigate(OrderManagementApp)
                        } else {
                            if (appUiState.isRegistered) {
                                navController.navigate(InventoryApp)
                            } else {

                            }
                        }
                    }
                }

            }
            authenticationNavGraph(navController) {
                navController.navigate(OrderManagementApp)
            }
            orderManagementNavGraph(navController)
            inventoryNavGraph(navController)
            settingsNavGraph(navController)
        }
    }
}