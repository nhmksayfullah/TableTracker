package app.tabletracker.feature_menu.ui.screen.category

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import app.tabletracker.feature_menu.data.entity.Category
import app.tabletracker.feature_menu.util.DatabaseOperation

@Composable
fun AddNewCategoryDialog(
    category: Category,
    databaseOperation: DatabaseOperation,
    modifier: Modifier = Modifier,
    onValueChange: (Category) -> Unit,
    onCreateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onUpdatedClick: () -> Unit,
    onShowFoodsClick: () -> Unit
) {


    Scaffold(
        bottomBar = {
            Column {
                when (databaseOperation) {
                    DatabaseOperation.Add -> {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            onClick = {
                                onCreateClick()
                            }
                        ) {
                            Text(text = "Create")
                        }
                    }

                    DatabaseOperation.Edit -> {
                        Row {
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp, end = 2.dp),
                                onClick = {
                                    onUpdatedClick()
                                }
                            ) {
                                Text(text = "Update")
                            }
                            Button(
                                modifier = Modifier
                                    .padding(start = 2.dp, end = 8.dp),
                                onClick = {
                                    onDeleteClick()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.onError,
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text(text = "Delete")
                            }
                        }
                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            onClick = {
                                onShowFoodsClick()
                            }
                        ) {
                            Text("Show Foods")
                        }
                    }

                    DatabaseOperation.Delete -> {}
                    DatabaseOperation.Read -> {}
                }


            }

        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .padding(8.dp)
        ) {
            TextField(
                value = category.name,
                onValueChange = {
                    onValueChange(category.copy(name = it))
                },
                label = {
                    Text(text = "Category name")
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
            )


            Text(text = "Print to Kitchen Copy?")
            Switch(
                checked = category.isKitchenCategory,
                onCheckedChange = {
                    onValueChange(category.copy(isKitchenCategory = it))
                }
            )
        }
    }
}
