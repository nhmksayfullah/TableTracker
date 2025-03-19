package app.tabletracker.feature_order.ui.screen.takeorder

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.tabletracker.feature_menu.data.entity.CategoryWithMenuItems
import app.tabletracker.feature_menu.data.entity.MenuItem

@Composable
fun MenuDisplayRightSection(
    menus: List<CategoryWithMenuItems>,
    modifier: Modifier = Modifier,
    onMenuItemClicked: (MenuItem) -> Unit
) {
    var isCategoryVisible by rememberSaveable {
        mutableStateOf(true)
    }
    BackHandler(true) {
        isCategoryVisible = true
    }
    var selectedCategoryId by remember {
        mutableStateOf(-1)
    }

    if (isCategoryVisible) {
        SelectCategoryRightSection(
            menus = menus.sortedBy { it.category.index },
            onCategoryClicked = {
                isCategoryVisible = false
                selectedCategoryId = it.id
            }
        )
    } else {
        menus.find {
            it.category.id == selectedCategoryId
        }?.let {
            if (it.menuItems.isNotEmpty()) {
                SelectMenuItemRightSection(
                    menus = it.menuItems.sortedBy { it.index },
                    onMenuItemClicked = onMenuItemClicked
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No Menu Items Available")
                }
            }
        }

    }
}