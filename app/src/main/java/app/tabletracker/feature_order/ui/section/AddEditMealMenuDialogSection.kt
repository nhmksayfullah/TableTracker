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
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_menu.ui.section.SelectableFoodBlock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMealMenuDialogSection(
//    menus: List<MenuItem>,
    menuItem: MenuItem,
//    selectedMealItemId: String,
    onMenuItemClicked: (MenuItem) -> Unit,
    onDialogDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
//    var selectedItemIds by rememberSaveable {
//        mutableStateOf(menus.find { it.id == selectedMealItemId }?.mealCourses?.map {
//            it.selectedItem?.id ?: ""
//        }?.toMutableList() ?: mutableListOf())
//    }
    var selectedItemIds by rememberSaveable {
        mutableStateOf(menuItem.mealCourses.map {
            it.selectedItem?.id ?: ""
        }.toMutableList())
    }

    BasicAlertDialog(
        onDismissRequest = { },
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 100.dp, vertical = 20.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            bottomBar = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            onDialogDismiss()
                        },
                        colors = ButtonDefaults.buttonColors()
                            .copy(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        var mealCourses = menuItem.mealCourses
                        mealCourses = mealCourses.mapIndexed { index, mealCourse ->
                            mealCourse.copy(
                                selectedItem = mealCourse.availableItems.find { it.id == selectedItemIds[index] }
                            )
                        }
                        onMenuItemClicked(menuItem.copy(mealCourses = mealCourses))
//                        val newMenuItem = menus.find { it.id == menuItem.id }
//                        if (newMenuItem != null) {
//                            var mealCourses = newMenuItem.mealCourses
//                            mealCourses = mealCourses.mapIndexed { index, it ->
//                                it.copy(
//                                    selectedItem = it.availableItems.find { it.id ==  selectedItemIds[index]}
//                                )
//                            }
//                            onMenuItemClicked(newMenuItem.copy(mealCourses = mealCourses))
//                        }

                        onDialogDismiss()
                    }) {
                        Text(text = "Add to Order")
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                TabbedScreen(titles = menuItem.mealCourses.map { it.name }) {mealCourseIndex ->

                    var loading by rememberSaveable {
                        mutableStateOf(true)
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(120.dp),
                        contentPadding = PaddingValues(8.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .then(modifier)
                    ) {
                        itemsIndexed(menuItem.mealCourses[mealCourseIndex].availableItems) {index, item ->
                            Box(modifier = Modifier.padding(4.dp)) {
                                key(loading) {
                                    SelectableFoodBlock(
                                        isSelected = item.id == selectedItemIds[mealCourseIndex],
                                        text = item.name
                                    ) {
                                        selectedItemIds[mealCourseIndex] = item.id
                                        loading = !loading
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}