package app.tabletracker.feature_order.ui.section

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.tabletracker.core.ui.TabbedScreen
import app.tabletracker.core.ui.component.FoodBlockComponent
import app.tabletracker.feature_menu.data.entity.Category
import app.tabletracker.feature_menu.data.entity.CategoryWithMenuItems
import app.tabletracker.feature_menu.data.entity.MenuItem

@Composable
fun ShowMenuRightSection(
    menus: List<CategoryWithMenuItems>,
    onCategoryClicked: (Category) -> Unit,
    onMenuItemClicked: (MenuItem) -> Unit,
) {
    var selectedCategoryIndex by rememberSaveable {
        if (menus.isEmpty()) mutableIntStateOf(0) else mutableIntStateOf((menus[0].category.id - 1))
    }

    var firstLaunch by rememberSaveable {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabbedScreen(
            titles = menus.map { it.category.name },
            onClick = {
                onCategoryClicked(menus[it].category)
            }
        ) {
            if (it == menus.size) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "It seems no item is available to make order with")
                }
                // TODO(open the left section for editing category)
            } else {
                selectedCategoryIndex = it
                if (firstLaunch) {
                    onCategoryClicked(menus[it].category)
                    firstLaunch = false
                }

                Spacer(modifier = Modifier.height(20.dp))
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(120.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(menus[selectedCategoryIndex].menuItems) { item ->
                        FoodBlockComponent(
                            text = item.name,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            onMenuItemClicked(item)
                        }
                    }
                }
            }
        }
    }
}