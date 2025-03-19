package app.tabletracker.feature_menu.ui.screen.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import app.tabletracker.feature_menu.data.entity.Category

@Composable
fun AddNewCategoryDialog(
    modifier: Modifier = Modifier,
    onCreateClick: (category: Category) -> Unit
) {

    var newCategory by remember {
        mutableStateOf(Category(
            name = "",
            isKitchenCategory = false
        ))
    }
    Scaffold(
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = {
                    if (newCategory.name.isNotEmpty()) {
                        onCreateClick(newCategory)
                    }
                }
            ) {
                Text(text = "Create")
            }
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .padding(8.dp)
        ) {
            TextField(
                value = newCategory.name,
                onValueChange = {
                    newCategory = newCategory.copy(name = it)
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
                checked = newCategory.isKitchenCategory,
                onCheckedChange = {
                    newCategory = newCategory.copy(isKitchenCategory = it)
                }
            )
        }
    }
}
