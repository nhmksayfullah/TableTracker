package app.tabletracker.feature_menu.ui.section

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.tabletracker.core.ui.TabbedScreen
import app.tabletracker.core.ui.component.FoodBlockComponent
import app.tabletracker.feature_menu.data.entity.CategoryWithMenuItems
import app.tabletracker.feature_menu.data.entity.MenuItem

@Composable
fun EditMenuItemAdvanceSection(
    menus: List<CategoryWithMenuItems>,
    menuItem: MenuItem,
    modifier: Modifier = Modifier,
    onMenuItemChange: (MenuItem) -> Unit
) {
    var mealSwitchState by rememberSaveable {
        mutableStateOf(menuItem.isMeal)
    }
    var dialogState by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        floatingActionButton = {
            if (mealSwitchState) {
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Make it a Meal")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = mealSwitchState,
                    onCheckedChange = {
                        mealSwitchState = it
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            for (i in emptyList<Int>()) {
                Card {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Starter",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(text = "Selected Item: 10")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            dialogState = true
                        }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }


        }
    }

    AddMealCourseDialogue(
        dialogState = dialogState,
        menus = menus,
        menuItem = menuItem,
        onDialogStateChange = {
            dialogState = it
        }
    ) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealCourseDialogue(
    dialogState: Boolean,
    menus: List<CategoryWithMenuItems>,
    menuItem: MenuItem,
    modifier: Modifier = Modifier,
    onDialogStateChange: (Boolean) -> Unit,
    onMenuItemChange: (MenuItem) -> Unit
) {
    if (dialogState) {

        BasicAlertDialog(
            onDismissRequest = {  },
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
                            onClick = { onDialogStateChange(false) },
                            colors = ButtonDefaults.buttonColors().copy(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { onDialogStateChange(false) }) {
                            Text(text = "Add Course")
                        }
                    }
                },
                topBar = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = "",
                            onValueChange = {},
                            label = {
                                Text(text = "Course Name")
                            },
                            placeholder = {
                                Text(text = "e.g. Starter")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                    }
                }
            ) {
                Column(
                    modifier = Modifier.padding(it)
                ) {
                    EditMealSection(
                        menus = menus,
                        menuItem = menuItem
                    )
                }
            }

        }

    }
}

@Composable
fun EditMealSection(
    menus: List<CategoryWithMenuItems>,
    menuItem: MenuItem,
    modifier: Modifier = Modifier
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
                Row{
                    Icon(imageVector = Icons.Default.Info, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Select items that you want to add on this Course")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { /*TODO*/ }) {
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
                                isSelected = false
                            ) {

                            }
                        }
                    } else {
                        Box(modifier = Modifier.padding(4.dp)) {
                            FoodBlockComponent(
                                text = it.name,
                                modifier = Modifier.clickable(false) {  },
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
