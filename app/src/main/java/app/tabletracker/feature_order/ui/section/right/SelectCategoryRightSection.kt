package app.tabletracker.feature_order.ui.section.right

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.tabletracker.core.ui.component.FoodBlockComponent
import app.tabletracker.feature_menu.data.entity.Category
import app.tabletracker.feature_menu.data.entity.CategoryWithMenuItems

@Composable
fun SelectCategoryRightSection(
    menus: List<CategoryWithMenuItems>,
    onCategoryClicked: (Category) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxSize().padding(8.dp).then(modifier)
    ) {
        items(menus) { item ->
            FoodBlockComponent(
                text = item.category.name,
                modifier = Modifier.padding(4.dp),
                containerColor = MaterialTheme.colorScheme.inversePrimary
            ) {
                onCategoryClicked(item.category)
            }
        }
    }
}