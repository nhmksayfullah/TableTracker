package app.tabletracker.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import app.tabletracker.di.TableTrackerContainer
import app.tabletracker.di.TableTrackerDataContainer
import app.tabletracker.core.database.TableTrackerDatabase
import app.tabletracker.features.companion.client.SocketClientService
import app.tabletracker.features.companion.server.SocketServerService

class TableTrackerApplication : Application() {
    companion object {
        const val DEVICE_TYPE_PREFERENCE_NAME = "device_type_preference"
    }

    lateinit var container: TableTrackerContainer

    override fun onCreate() {
        super.onCreate()


        val database: TableTrackerDatabase by lazy { TableTrackerDatabase.createDatabase(this) }

        container = TableTrackerDataContainer(
            database = database,
            dataStore = dataStore
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serverChannel = NotificationChannel(
                SocketServerService.NOTIFICATION_CHANNEL,
                "Server Running",
                NotificationManager.IMPORTANCE_HIGH
            )
            val clientChannel = NotificationChannel(
                SocketClientService.NOTIFICATION_CHANNEL,
                "Client Running",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(serverChannel)
            notificationManager.createNotificationChannel(clientChannel)
        }

    }


}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = TableTrackerApplication.DEVICE_TYPE_PREFERENCE_NAME
)

// access the application class from the initializer of the viewmodel provider.
fun CreationExtras.accessTableTrackerApplication(): TableTrackerApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TableTrackerApplication)

