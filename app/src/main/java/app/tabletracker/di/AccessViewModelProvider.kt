package app.tabletracker.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.tabletracker.app.accessTableTrackerApplication
import app.tabletracker.app.ui.AppViewModel
import app.tabletracker.features.auth.ui.AuthViewModel
import app.tabletracker.features.inventory.ui.EditMenuViewModel
import app.tabletracker.features.order.ui.state.OrderViewModel
import app.tabletracker.features.settings.ui.SettingsViewModel

object AccessViewModelProvider {
    val Factory = viewModelFactory {
        // initialize every viewmodel of the app here with a new initializer {} block.
        initializer {
            AppViewModel(repository = accessTableTrackerApplication().container.applicationRepository)
        }
        initializer {
            EditMenuViewModel(repository = accessTableTrackerApplication().container.editMenuRepository)
        }
        initializer {
            OrderViewModel(
                orderRepo = accessTableTrackerApplication().container.orderRepository,
                deviceTypeRepo = accessTableTrackerApplication().container.deviceTypeRepository
            )
        }
        initializer {
            AuthViewModel(
                authRepo = accessTableTrackerApplication().container.authRepository,
                deviceTypeRepo = accessTableTrackerApplication().container.deviceTypeRepository,
                editMenuRepository = accessTableTrackerApplication().container.editMenuRepository
            )
        }
        initializer {
            SettingsViewModel(repository = accessTableTrackerApplication().container.settingsRepository)
        }
    }
}