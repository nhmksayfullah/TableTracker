package app.tabletracker.core.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DisabledTextField(
    modifier: Modifier = Modifier,
    label: String = "",
    value: String,
    onClink: () -> Unit
) {
    Card(
        onClick = onClink,
        modifier = modifier,
        colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (label.isNotEmpty()) {
                Text(text = label, style = MaterialTheme.typography.labelSmall)
            }
            Text(text = value)
        }
    }
}