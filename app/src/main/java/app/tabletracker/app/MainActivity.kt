package app.tabletracker.app

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import app.tabletracker.theme.TableTrackerTheme


const val MADRAS_SPICE_LICENCE_KEY = "87222f7a-149f-4d14-99a2-2283eaf797d3"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if((resources.configuration.screenLayout
                    and Configuration.SCREENLAYOUT_SIZE_MASK)
            >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        actionBar?.hide()
        setContent {
            TableTrackerTheme {
                LaunchTableTracker(this)
            }
        }
    }
}

