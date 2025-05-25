package app.tabletracker.features.order.ui.screen.startorder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import app.tabletracker.theme.MaterialColor

@Composable
fun ConnectSection(
    modifier: Modifier = Modifier,
    onConnectClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.End
    ) {
        FilterChip(
            selected = true,
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialColor.Blue.color,
                selectedLabelColor = Color.White
            ),
            onClick = onConnectClick,
            label = {
                Text(
                    text = "Connect"
                )
            }
        )
    }
}