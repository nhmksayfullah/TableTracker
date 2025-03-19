package app.tabletracker.util

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import app.tabletracker.app.config.TableTrackerContainer
import app.tabletracker.app.config.TableTrackerDataContainer
import app.tabletracker.app.data.local.TableTrackerDatabase

class TableTracker : Application() {


    lateinit var container: TableTrackerContainer

    override fun onCreate() {
        super.onCreate()

        val database: TableTrackerDatabase by lazy { TableTrackerDatabase.createDatabase(this) }
        container = TableTrackerDataContainer(database)

    }

}

// access the application class from the initializer of the viewmodel provider.
fun CreationExtras.accessTableTrackerApplication(): TableTracker =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TableTracker)