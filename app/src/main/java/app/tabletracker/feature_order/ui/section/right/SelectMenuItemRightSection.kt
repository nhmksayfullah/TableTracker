package app.tabletracker.feature_order.ui.section.right

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import app.tabletracker.core.ui.component.FoodBlockComponent
import app.tabletracker.feature_menu.data.entity.MenuItem

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
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .then(modifier)
    ) {
        items(menus) { item ->
            FoodBlockComponent(
                text = item.name,
                modifier = Modifier.padding(4.dp)
            ) {
                if (item.isMeal && item.mealCourses.isNotEmpty()) {
                    selectedMealItemId = item.id
                    dialogState = true
                } else {
                    onMenuItemClicked(item)
                }
            }
        }
    }

    if (dialogState) {
//        menus.find { it.id == selectedMealItemId }?.let {
//            AddEditMealMenuDialogSection(
//                menuItem = it,
//                onMenuItemClicked = onMenuItemClicked,
//                onDialogDismiss = { dialogState = false }
//            )
//        }

    }

}