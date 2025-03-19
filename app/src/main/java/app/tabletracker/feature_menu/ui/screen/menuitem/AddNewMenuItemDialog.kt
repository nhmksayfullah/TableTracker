package app.tabletracker.feature_menu.ui.screen.menuitem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import app.tabletracker.feature_menu.data.entity.Category
import app.tabletracker.feature_menu.data.entity.MenuItem

@Composable
fun AddNewMenuItemDialog(
    selectedCategory: Category,
    modifier: Modifier = Modifier,
    onCreateClick: (menuItem: MenuItem) -> Unit
) {

    var newMenuItem by remember {
        mutableStateOf(
            MenuItem(
                name = "",
                abbreviation = "",
                prices = mapOf(),
                categoryId = selectedCategory.id,
                isKitchenCategory = selectedCategory.isKitchenCategory
            )
        )
    }
    Scaffold(
        bottomBar = {

        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {

        }
    }

}