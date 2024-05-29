package app.tabletracker.feature_order.ui.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import app.tabletracker.core.ui.TabbedScreen
import app.tabletracker.core.ui.component.FoodBlockComponent
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_menu.ui.section.SelectableFoodBlock

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
        menus.find { it.id == selectedMealItemId }?.let {
            AddEditMealMenuDialogSection(
                menuItem = it,
                onMenuItemClicked = onMenuItemClicked,
                onDialogDismiss = { dialogState = false }
            )
        }

    }

}