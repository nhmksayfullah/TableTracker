package app.tabletracker.features.order.ui.screen.takeorder

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.tabletracker.core.ui.component.MenuItemComponent
import app.tabletracker.features.inventory.data.entity.MenuItem

@Composable
fun SelectMenuItemRightSection(
    menus: List<MenuItem>,
    onMenuItemClicked: (MenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var dialogState by rememberSaveable {
        mutableStateOf(false)
    }
    var selectedMealItemId by rememberSaveable {
        mutableStateOf("")
    }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        items(menus) { item ->
            MenuItemComponent(
                title = item.name,
                onClick = {
                    if (item.isMeal && item.mealCourses.isNotEmpty()) {
                        selectedMealItemId = item.id
                        dialogState = true
                    } else {
                        onMenuItemClicked(item)
                    }
                }
            )
        }
    }

}