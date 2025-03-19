package app.tabletracker.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier


@Composable
fun TabbedScreen(
    titles: List<String>,
    modifier: Modifier = Modifier,
    tabContent: @Composable (state: Int) -> Unit,
) {
    var state by rememberSaveable {
        mutableIntStateOf(0)
    }
    Column(
        modifier = modifier
    ) {
        TabRow(
            selectedTabIndex = state,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state == index,
                    onClick = {
                        state = index
                    },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall
                        )
                    },
                )
            }
        }
        tabContent(state)
    }
}