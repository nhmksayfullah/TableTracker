package app.tabletracker.features.order.ui.screen.startorder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.tabletracker.R
import app.tabletracker.features.order.ui.component.OverViewComponent
import app.tabletracker.theme.MaterialColor

@Composable
fun OverViewSection(
    totalTransaction: Float,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        modifier = modifier
            .fillMaxWidth()
    ) {
        OverViewComponent(
            title = "Total orders",
            value = "45",
            icon = R.drawable.outline_shopping_bag_24,
            iconTint = MaterialColor.Amber.color
        )
        OverViewComponent(
            title = "Pending orders",
            value = "8",
            icon = R.drawable.round_pending_actions_24,
            iconTint = MaterialColor.Blue.color
        )
        OverViewComponent(
            title = "Avg prep time",
            value = "~23 mins",
            icon = R.drawable.outline_schedule_24,
            iconTint = MaterialColor.Green.color
        )
    }
}