package app.tabletracker.app.version

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.tabletracker.app.ui.AppUiState
import app.tabletracker.auth.AuthenticationApp
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
    var showBottomBar by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            navBackStackEntry?.destination?.let {
                showBottomBar = !it.hasRoute(Screen.TakeOrderScreen::class)
            }
            showBottomBar = appUiState.hasInventory
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 500)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 500)
                )
            ) {

                SetupBottomNavigationBar(
                    onNavigationItemClick = { navOption ->
                        navBackStackEntry?.destination?.let {
                            if (!it.hasRoute(navOption.navOption.route::class)) {
                                if (navOption.navOption.route == Screen.StartOrderScreen) {
                                    navController.navigate(navOption.navOption.route) {
                                        popUpTo(Screen.StartOrderScreen) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                } else {
                                    navController.navigate(navOption.navOption.route) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        }


                    }
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.LoadingScreen,
            modifier = Modifier.padding(it)
        ) {
            composable<Screen.LoadingScreen>(
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            durationMillis = 1000
                        )
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            durationMillis = 1000
                        )
                    )
                }
            ) {
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
                            navController.navigate(OrderManagementApp) {
                                popUpTo(Screen.LoadingScreen) {
                                    inclusive = true
                                }
                            }
                        } else {
                            if (appUiState.isRegistered) {
                                navController.navigate(InventoryApp) {
                                    popUpTo(Screen.LoadingScreen) {
                                        inclusive = true
                                    }
                                }
                            } else {
                                navController.navigate(AuthenticationApp) {
                                    popUpTo(Screen.LoadingScreen) {
                                        inclusive = true
                                    }
                                }
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