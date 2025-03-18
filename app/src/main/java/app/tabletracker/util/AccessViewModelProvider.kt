package app.tabletracker.util

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.tabletracker.app.ui.AppViewModel
import app.tabletracker.auth.ui.AuthViewModel
import app.tabletracker.feature_menu.ui.EditMenuViewModel
import app.tabletracker.feature_order.ui.state.OrderViewModel
import app.tabletracker.settings.ui.SettingsViewModel

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
                orderRepo = accessTableTrackerApplication().container.orderRepository
            )
        }
        initializer {
            AuthViewModel(repository = accessTableTrackerApplication().container.authRepository)
        }
        initializer {
            SettingsViewModel(repository = accessTableTrackerApplication().container.settingsRepository)
        }
    }
}