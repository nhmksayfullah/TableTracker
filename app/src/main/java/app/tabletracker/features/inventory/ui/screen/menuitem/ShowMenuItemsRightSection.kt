package app.tabletracker.features.inventory.ui.screen.menuitem

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
import app.tabletracker.core.ui.component.MenuItemComponent
import app.tabletracker.features.inventory.data.entity.MenuItem
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState


@Composable
fun ShowMenuItemsRightSection(
    menuItems: List<MenuItem>,
    modifier: Modifier = Modifier,
    onMenuItemClicked: (MenuItem) -> Unit,
    onAddNewMenuItem: () -> Unit,
    onMenuItemsReordered: (List<MenuItem>) -> Unit,
) {

    var canDrag by remember {
        mutableStateOf(false)
    }
    val lazyGridState = rememberLazyGridState()

    var updatedMenuItems by remember {
        mutableStateOf(menuItems)
    }
    LaunchedEffect(menuItems) {
        updatedMenuItems = menuItems.sortedBy { it.index }
    }

    val reorderableLazyGridState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
        updatedMenuItems = updatedMenuItems.toMutableList().apply {
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
                            onMenuItemsReordered(updatedMenuItems)
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
                    onClick = onAddNewMenuItem,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Text("New Item")
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
            items(updatedMenuItems, key = { it.id }) { menuItem ->
                ReorderableItem(
                    reorderableLazyGridState,
                    key = menuItem.id
                ) {
                    MenuItemComponent(
                        title = menuItem.name,
                        modifier = Modifier
                            .padding(2.dp)
                    ) {
                        onMenuItemClicked(menuItem)
                    }
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


