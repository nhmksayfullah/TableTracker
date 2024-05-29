package app.tabletracker.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun SegmentedFoodBlocks(
    labels: List<String>,
    modifier: Modifier = Modifier,
    onSelected: (Int) -> Unit
) {
    var selected by rememberSaveable { mutableStateOf(-1) }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .then(modifier)
    ) {

        itemsIndexed(labels) { index: Int, label: String ->
            Box(modifier = Modifier.padding(4.dp)) {
                FoodBlockComponent(
                    text = label,
                    containerColor = if (selected == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                    contentColor = if (selected == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                ) {
                    selected = index
                    if (selected != -1) {
                        onSelected(index)
                    }
                }
            }
        }
    }
}