package app.tabletracker.features.order.ui.screen.takeorder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.tabletracker.core.ui.component.CategoryComponent
import app.tabletracker.features.inventory.data.entity.Category
import app.tabletracker.features.inventory.data.entity.CategoryWithMenuItems

@Composable
fun SelectCategoryRightSection(
    menus: List<CategoryWithMenuItems>,
    onCategoryClicked: (Category) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        items(menus) { item ->
            Box(
                modifier = Modifier
                    .padding(2.dp)
            ) {
                CategoryComponent(
                    text = item.category.name,
                    onClick = {
                        onCategoryClicked(item.category)
                    },
                    containerColor = item.category.color
                )
            }
        }
    }
}
