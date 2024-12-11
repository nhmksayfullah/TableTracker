package app.tabletracker.util

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.tabletracker.app.ui.AppViewModel
import app.tabletracker.auth.ui.AuthViewModel
import app.tabletracker.feature_menu.ui.EditMenuViewModel
import app.tabletracker.feature_order.ui.OrderViewModel
import app.tabletracker.feature_order.v2.state.OrderViewModel2
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
                orderRepo = accessTableTrackerApplication().container.orderRepository,
                customerRepo = accessTableTrackerApplication().container.customerRepository
            )
        }
        initializer {
            AuthViewModel(repository = accessTableTrackerApplication().container.authRepository)
        }
        initializer {
            SettingsViewModel(repository = accessTableTrackerApplication().container.settingsRepository)
        }
        initializer {
            OrderViewModel2(repository = accessTableTrackerApplication().container.orderRepository)
        }
    }
}