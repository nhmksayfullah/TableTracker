package app.tabletracker.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import app.tabletracker.di.AccessViewModelProvider

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.accessSharedViewModel(navController: NavController): T {
    val navGraphRoute =
        destination.parent?.route ?: return viewModel(factory = AccessViewModelProvider.Factory)
    val parentEntry = remember(key1 = this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(viewModelStoreOwner = parentEntry, factory = AccessViewModelProvider.Factory)

}