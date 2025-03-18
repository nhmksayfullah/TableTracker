package app.tabletracker.app

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import app.tabletracker.app.ui.AppViewModel
import app.tabletracker.app.version.LargeScreenApp
import app.tabletracker.theme.TableTrackerTheme
import app.tabletracker.util.AccessViewModelProvider

// this is the uppermost function that will launch different table tracker apps based on the window size.
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun LaunchTableTracker(
    activity: Activity,
    modifier: Modifier = Modifier
) {


    val context = LocalContext.current
    val appViewModel: AppViewModel = viewModel(factory = AccessViewModelProvider.Factory)
    val appUiState by appViewModel.uiState.collectAsStateWithLifecycle()
    // calculates the windowSize and launch.
    val windowSize = calculateWindowSizeClass(activity = context as Activity).widthSizeClass




    // show splash screen until all data has fetched
    activity.installSplashScreen().apply {
        setKeepOnScreenCondition {
            appUiState.loading
        }
    }


    TableTrackerTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when(windowSize) {
                // this app for mobile phones
                WindowWidthSizeClass.Compact -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "The app is not available for this screen size yet );")
                    }
                }
                // this app for small size tablets
                WindowWidthSizeClass.Medium -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "The app is not available for this screen size yet );")
                    }
                }
                // this app for big screens.
                WindowWidthSizeClass.Expanded -> {

                    LargeScreenApp(
                        appUiState = appUiState,
                    )
                }
            }
        }
    }
}