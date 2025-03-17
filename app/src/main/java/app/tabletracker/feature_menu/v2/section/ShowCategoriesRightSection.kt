package app.tabletracker.feature_menu.v2.section

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.tabletracker.core.ui.component.FoodBlockComponent
import app.tabletracker.feature_menu.data.entity.Category
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.theme.MaterialColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowCategoriesRightSection(
    categories: List<Category>,
    modifier: Modifier = Modifier,
    onCategoryClicked: (Category) -> Unit,
    onCategoryDoubleClicked: (Category) -> Unit,
    onAddNewCategory: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddNewCategory
            ) {
                Text("New Category")
            }
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(120.dp),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.padding(it)
        ) {
            items(categories) { category ->
                FoodBlockComponent(
                    text = category.name,
                    modifier = modifier
                        .padding(4.dp)
                        .combinedClickable(
                            onClick = {
                                onCategoryClicked(category)
                            },
                            onDoubleClick = {
                                onCategoryDoubleClicked(category)
                            }
                        ),
                    containerColor = MaterialColor.Blue.color
                ) {
//                onCategoryClicked(category)
                }
            }
        }
    }
}

@Composable
fun ShowMenuItemsRightSection(
    menuItems: List<MenuItem>,
    modifier: Modifier = Modifier,
    onMenuItemClicked: (MenuItem) -> Unit,
    onAddNewMenuItem: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddNewMenuItem
            ) {
                Text("New Item")
            }
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(120.dp),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.padding(it)
        ) {
            items(menuItems) {
                FoodBlockComponent(
                    text = it.name,
                    modifier = modifier.padding(4.dp)
                ) {
                    onMenuItemClicked(it)
                }
            }
        }
    }
}