package app.tabletracker.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings

sealed class ExtraNavOption(val navOption: NavigationOption) {

    data object Settings: ExtraNavOption(
        NavigationOption(
            id = 0,
            title = "Settings",
            icon = Icons.Default.Settings,
            route = Screen.SettingsScreen.route
        )
    )
    data object Edit: ExtraNavOption(
        NavigationOption(
            id = 1,
            title = "Edit",
            icon = Icons.Default.Edit,
            route = null
        )
    )
    data object Done: ExtraNavOption(
        NavigationOption(
            id = 2,
            title = "Done",
            icon = Icons.Default.Done,
            route = null
        )
    )
}