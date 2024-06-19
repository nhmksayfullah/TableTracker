package app.tabletracker.settings.ui.section

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.tabletracker.core.ui.SplitScreen

@Composable
fun DiscountSettingsSection(modifier: Modifier = Modifier) {
    SplitScreen(
        leftContent = { /*TODO*/ },
        rightContent = { /*TODO*/ },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {

            }) {
                Text(text = "Add New Discount")
            }
        }
    )
}