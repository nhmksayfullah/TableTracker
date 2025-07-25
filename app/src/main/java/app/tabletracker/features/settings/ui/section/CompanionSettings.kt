package app.tabletracker.features.settings.ui.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CompanionSettings(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            Button(
                onClick = {}
            ) {
                Text("Connect with Companion")
            }
        }
    }
}