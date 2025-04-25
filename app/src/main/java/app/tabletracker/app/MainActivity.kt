package app.tabletracker.app

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import app.tabletracker.features.auth.data.model.Restaurant
import app.tabletracker.theme.TableTrackerTheme

val MADRAS_SPICE_RESTAURANT = Restaurant(
    name = "Madras Spice Restaurant",
    address = "180 Northenden Rd, Sale M33 2SR",
    contactNumber = "07123456789",
    licence = "87222f7a-149f-4d14-99a2-2283eaf797d3",
    website = "www.madras-spice.uk",
    vatNumber = "303043464"
)
// IP address: 192.168.1.249:


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if ((resources.configuration.screenLayout
                    and Configuration.SCREENLAYOUT_SIZE_MASK)
            >= Configuration.SCREENLAYOUT_SIZE_LARGE
        ) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        actionBar?.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        setContent {
            TableTrackerTheme {
                LaunchTableTracker(this)
            }
        }
    }
}

