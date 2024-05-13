package app.tabletracker.core.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationOption(
    val id: Int,
    val title: String,
    val icon: ImageVector,
    val route: String?
)