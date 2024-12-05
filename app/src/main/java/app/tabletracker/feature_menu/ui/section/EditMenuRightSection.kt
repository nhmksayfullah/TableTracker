package app.tabletracker.feature_menu.ui.section

import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import app.tabletracker.core.ui.TabbedScreen
import app.tabletracker.core.ui.component.FoodBlockComponent
import app.tabletracker.feature_menu.data.entity.Category
import app.tabletracker.feature_menu.data.entity.CategoryWithMenuItems
import app.tabletracker.feature_menu.data.entity.MenuItem
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMenuRightSection(
    menus: List<CategoryWithMenuItems>,
    onCategoryClicked: (Category) -> Unit,
    onMenuItemClicked: (MenuItem) -> Unit,
    onAddNewCategory: () -> Unit,
    onAddNewMenuItem: () -> Unit,
    onUpdateCategories: (List<Category>) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategoryIndex by rememberSaveable {
        if (menus.isEmpty()) mutableStateOf(0) else mutableStateOf((menus[0].category.id))
    }

    var firstLaunch by rememberSaveable {
        mutableStateOf(true)
    }
    var showReorderCategoryDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                showReorderCategoryDialog = true
            }
            ) {
                Text(text = "Reorder Category")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabbedScreen(
                titles = menus.sortedBy { it.category.index }.map { it.category.name } + listOf("+"),
                onClick = {
                    Log.d("it: menu.size:- ", "$it : ${menus.size}")
                    if (it == menus.size) {
                        onAddNewCategory()
                    } else {
                        onCategoryClicked(menus[it].category)
                    }
//                Log.d("selected category: ", menus[it].category.toString())
                }
            ) {
                if (it == menus.size) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Create a Category first to add menu item on it")
                    }
                    // TODO(open the left section for editing category)
                } else {
                    selectedCategoryIndex = it
                    if (firstLaunch) {
                        val selectedCategory = menus.find { it.category.index == selectedCategoryIndex }?.category
                        if (selectedCategory != null) {
                            onCategoryClicked(selectedCategory)
                        }

                        firstLaunch = false
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(120.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(menus[selectedCategoryIndex].menuItems) {

                            Box(modifier = Modifier.padding(4.dp)) {
                                FoodBlockComponent(text = it.name) {
                                    onMenuItemClicked(it)
                                }
                            }
                        }
                        item {
                            Box(modifier = Modifier.padding(4.dp)) {
                                FoodBlockComponent(text = "+") {
                                    onAddNewMenuItem()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (showReorderCategoryDialog) {
        BasicAlertDialog(
            onDismissRequest = { showReorderCategoryDialog = false },
            modifier = Modifier.fillMaxSize()
        ) {
            ReorderCategoryDialog(
                menus = menus,
                onUpdateCategories = onUpdateCategories,
                onDismiss = {
                    showReorderCategoryDialog = false
                }
            )

        }
    }
}




@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReorderCategoryDialog(
    menus: List<CategoryWithMenuItems>,
    onUpdateCategories: (List<Category>) -> Unit,
    onDismiss: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val view = LocalView.current

    val mutableMenus = remember {
        mutableStateListOf<Category>()
    }
    var orderChanged by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = menus) {
        mutableMenus.clear()
        val categoryList = menus.map { it.category }
        mutableMenus.addAll(categoryList)
        mutableMenus.sortBy {
            it.index
        }
    }
    var isCategoryVisible by rememberSaveable {
        mutableStateOf(true)
    }
    BackHandler(true) {
        isCategoryVisible = true
    }


    val categoryGridState = rememberLazyGridState()
    val reorderableCategoryGridState =
        rememberReorderableLazyGridState(categoryGridState) { from, to ->
            mutableMenus.apply {
                add(to.index, removeAt(from.index))
            }
            orderChanged = true
            view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
        }

    Scaffold(
        floatingActionButton = {
            if (orderChanged) {
                ExtendedFloatingActionButton(onClick = {
                    onUpdateCategories(mutableMenus.map { it.copy(index = mutableMenus.indexOf(it)) })
                    orderChanged = false
                },
                    modifier = Modifier.padding(16.dp)) {
                    Text(text = "Update Reorder")
                }
            }
        },
        topBar = {
            Column(
                modifier = Modifier.padding(4.dp)
            ) {
                IconButton(onClick = {onDismiss()}) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                }
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(120.dp),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
                .then(modifier),
            state = categoryGridState
        ) {
            items(mutableMenus, key = { it.index }) { item ->
                ReorderableItem(state = reorderableCategoryGridState, key = item.index) { isDragging ->

                    FoodBlockComponent(
                        text = item.name,
                        modifier = Modifier
                            .padding(4.dp)
                            .draggableHandle(
                                onDragStarted = {
                                    view.performHapticFeedback(HapticFeedbackConstants.DRAG_START)
                                },
                                onDragStopped = {
                                    view.performHapticFeedback(HapticFeedbackConstants.GESTURE_END)

                                }
                            )
                    ) {
                    }
                }
            }
        }
    }
}