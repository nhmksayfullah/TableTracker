package app.tabletracker.features.inventory.ui.screen.category

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import app.tabletracker.R
import app.tabletracker.core.ui.component.CategoryComponent
import app.tabletracker.features.inventory.data.entity.Category
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState

@Composable
fun ShowCategoriesRightSection(
    categories: List<Category>,
    modifier: Modifier = Modifier,
    onCategoryClicked: (Category) -> Unit,
    onAddNewCategory: () -> Unit,
    onCategoryReordered: (List<Category>) -> Unit
) {
    var canDrag by remember {
        mutableStateOf(false)
    }
    val lazyGridState = rememberLazyGridState()

    var updatedCategories by remember {
        mutableStateOf(categories)
    }
    LaunchedEffect(categories) {
        updatedCategories = categories.sortedBy { it.index }
    }

    val reorderableLazyGridState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
        updatedCategories = updatedCategories.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            Row {
                FloatingActionButton(
                    onClick = {
                        if (canDrag) {
                            canDrag = false
                            onCategoryReordered(updatedCategories)
                        } else {
                            canDrag = true
                        }
                    },
                    contentColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    if (canDrag) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Reorder End"
                        )
                    } else {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.baseline_swap_calls_24),
                            contentDescription = "Reorder Start"
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                ExtendedFloatingActionButton(
                    onClick = onAddNewCategory,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Text("New Category")
                }
            }
        }
    ) {
        LazyVerticalGrid(
            state = lazyGridState,
            columns = GridCells.Adaptive(120.dp),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            items(updatedCategories, key = { it.id }) { category ->
                ReorderableItem(
                    reorderableLazyGridState,
                    key = category.id,
                    modifier = Modifier
                        .padding(2.dp)
                ) {
                    CategoryComponent(
                        text = category.name,
                        onClick = {
                            onCategoryClicked(category)
                        },
                        containerColor = category.color

                    )
                    if (canDrag) {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.draggableHandle()
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.baseline_drag_indicator_24),
                                contentDescription = "Drag Icon"
                            )
                        }
                    }
                }

            }
        }
    }
}
