package app.tabletracker.feature_menu.ui.section

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import app.tabletracker.core.ui.TabbedScreen
import app.tabletracker.core.ui.component.FoodBlockComponent
import app.tabletracker.feature_menu.data.entity.CategoryWithMenuItems
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_menu.data.entity.meal.MealCourse
import app.tabletracker.feature_menu.domain.usecase.addItemToMealCourse
import app.tabletracker.feature_menu.domain.usecase.addItemsToMealCourse
import app.tabletracker.feature_menu.domain.usecase.addNewMealCourse
import app.tabletracker.feature_menu.domain.usecase.contains
import app.tabletracker.feature_menu.domain.usecase.findMealCourse
import app.tabletracker.feature_menu.domain.usecase.removeItemFromMealCourse
import app.tabletracker.feature_menu.domain.usecase.removeMealCourse
import app.tabletracker.feature_menu.domain.usecase.updateMealCourse

@Composable
fun EditMenuItemAdvanceSection(
    menus: List<CategoryWithMenuItems>,
    menuItem: MenuItem,
    modifier: Modifier = Modifier,
    onMenuItemChange: (MenuItem) -> Unit
) {
    var dialogState by rememberSaveable {
        mutableStateOf(false)
    }
    var selectedMealCourseId by rememberSaveable {
        mutableStateOf("")
    }

    Scaffold(
        floatingActionButton = {
            if (menuItem.isMeal) {
                ExtendedFloatingActionButton(onClick = {
                    dialogState = true
                }) {
                    Text(text = "Add new Course")
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Make it a Meal")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = menuItem.isMeal,
                    onCheckedChange = {
                        onMenuItemChange(menuItem.copy(isMeal = it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (menuItem.mealCourses.isNotEmpty()) {
                menuItem.mealCourses.forEach {
                    Card {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(text = "Selected Item: ${it.availableItems.size}")
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = {
                                selectedMealCourseId = it.id
                                dialogState = true
                            }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (menuItem.isMeal) {
                        Text(text = "No Course Available")
                    }
                }
            }


        }
    }

    key(dialogState) {
        AddMealCourseDialogue(
            dialogState = dialogState,
            menus = menus,
            menuItem = menuItem,
            mealCourse = menuItem.findMealCourse(selectedMealCourseId) ?: MealCourse(),
            onDialogStateChange = {
                dialogState = it
                selectedMealCourseId = ""
            },
            onChangeSelectedCourseId = {
                selectedMealCourseId = it
            }
        ) {
            onMenuItemChange(it)

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealCourseDialogue(
    dialogState: Boolean,
    menus: List<CategoryWithMenuItems>,
    menuItem: MenuItem,
    mealCourse: MealCourse,
    modifier: Modifier = Modifier,
    onChangeSelectedCourseId: (String) -> Unit = {},
    onDialogStateChange: (Boolean) -> Unit,
    onMenuItemChange: (MenuItem) -> Unit
) {
    var courseName by rememberSaveable {
        mutableStateOf(mealCourse.name)
    }

    if (dialogState) {

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
                                onDialogStateChange(false)
                                courseName = ""
                            },
                            colors = ButtonDefaults.buttonColors()
                                .copy(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            if (courseName.isNotEmpty()) {
                                if (menuItem.contains(mealCourse)) {
                                    onMenuItemChange(menuItem.updateMealCourse(mealCourse.copy(name = courseName)))
                                } else {
                                    onMenuItemChange(menuItem.addNewMealCourse(mealCourse.copy(name = courseName)))
                                }

                                onDialogStateChange(false)
                                courseName = ""
                            }

                        }) {
                            Text(text = "Add Course")
                        }
                    }
                },
                topBar = {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = courseName,
                            onValueChange = {
                                courseName = it
                            },
                            label = {
                                Text(text = "Course Name")
                            },
                            placeholder = {
                                Text(text = "e.g. Starter")
                            },
                            modifier = Modifier
                                .weight(8f)
                        )

                        if (menuItem.contains(mealCourse)) {
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    onMenuItemChange(menuItem.removeMealCourse(mealCourse))
                                    onDialogStateChange(false)
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                                )
                            ) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                            }
                        }
                    }
                }
            ) {
                Column(
                    modifier = Modifier.padding(it)
                ) {
                    EditMealSection(
                        menus = menus,
                        menuItem = menuItem,
                        mealCourse = mealCourse,
                        courseName = courseName,
                        onChangeSelectedCourseId = onChangeSelectedCourseId
                    ) {
                        onMenuItemChange(it)
                    }
                }
            }

        }

    }
}

@Composable
fun EditMealSection(
    menus: List<CategoryWithMenuItems>,
    menuItem: MenuItem,
    mealCourse: MealCourse,
    courseName: String,
    modifier: Modifier = Modifier,
    onChangeSelectedCourseId: (String) -> Unit = {},
    onMenuItemChange: (MenuItem) -> Unit
) {
    var selectedCategoryIndex by rememberSaveable {
        mutableStateOf(0)
    }
    TabbedScreen(titles = menus.map { it.category.name }) {
        selectedCategoryIndex = it
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Give it a name first, then select items which you want to add on this Course")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {
                    if (courseName.isNotEmpty()) {
                        val newMealCourse = mealCourse.copy(name = courseName)
                        val newMenuItem = menuItem.addNewMealCourse(newMealCourse)
                        onMenuItemChange(
                            newMenuItem.addItemsToMealCourse(newMealCourse, menus[selectedCategoryIndex].menuItems)
                        )
                        onChangeSelectedCourseId(mealCourse.id)
                    }
                }) {
                    Text(text = "Select All")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyVerticalGrid(
                columns = GridCells.Adaptive(120.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(menus[selectedCategoryIndex].menuItems) {

                    if (it.id != menuItem.id) {
                        Box(modifier = Modifier.padding(4.dp)) {
                            SelectableFoodBlock(
                                text = it.name,
                                isSelected = mealCourse.contains(it)
                            ) {
                                if (menuItem.contains(mealCourse)) {
                                    if (mealCourse.contains(it)) {
                                        onMenuItemChange(
                                            menuItem.removeItemFromMealCourse(mealCourse, it)
                                        )
                                    } else {
                                        onMenuItemChange(
                                            menuItem.addItemToMealCourse(mealCourse, it)
                                        )
                                    }
                                } else {
                                    if (courseName.isNotEmpty()) {
                                        val newMealCourse = mealCourse.copy(name = courseName)
                                        val newMenuItem = menuItem.addNewMealCourse(newMealCourse)
                                        onMenuItemChange(
                                            newMenuItem.addItemToMealCourse(newMealCourse, it)
                                        )
                                        onChangeSelectedCourseId(mealCourse.id)
                                    }
                                }
                            }
                        }
                    } else {
                        Box(modifier = Modifier.padding(4.dp)) {
                            FoodBlockComponent(
                                text = it.name,
                                modifier = Modifier.clickable(false) { },
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            ) {

                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectableFoodBlock(
    isSelected: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    onClick: () -> Unit
) {
    FoodBlockComponent(
        text = text,
        modifier = modifier,
        textModifier = textModifier,
        textStyle = textStyle,
        containerColor = if (isSelected) contentColor else containerColor,
        contentColor = if (isSelected) containerColor else contentColor,
    ) {
        onClick()
    }
}
