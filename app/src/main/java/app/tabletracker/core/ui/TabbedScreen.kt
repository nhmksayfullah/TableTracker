package app.tabletracker.core.ui

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabbedScreen(
    titles: List<String>,
    modifier: Modifier = Modifier,
    onClick: (state: Int) -> Unit = {},
    tabContent: @Composable (state: Int) -> Unit,
) {
    var state by rememberSaveable {
        mutableIntStateOf(0)
    }
    Column(
        modifier = Modifier.fillMaxSize().then(modifier)
    ) {
        PrimaryScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = state,
            indicator = @Composable {
                if (state < it.size) {
                    val width by animateDpAsState(targetValue = it[state].contentWidth, label = "DpAnimation")
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(it[state]),
                        width = width
                    )
                }
            }) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state == index,
                    onClick = {
                        state = index
                        Log.d("index: ", index.toString())
                        onClick(state)
                    },
                    text = { Text(text = title) },
                )
            }

        }
        tabContent(state)
    }
}