package app.tabletracker.feature_order.ui.section

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.tabletracker.core.ui.component.FoodBlockComponent
import app.tabletracker.feature_menu.data.entity.MenuItem

@Composable
fun SelectMenuItemRightSection(
    menus: List<MenuItem>,
    onMenuItemClicked: (MenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxSize().padding(8.dp).then(modifier)
    ) {
        items(menus) { item ->
            FoodBlockComponent(
                text = item.name,
                modifier = Modifier.padding(4.dp)
            ) {
                onMenuItemClicked(item)
            }
        }
    }
}