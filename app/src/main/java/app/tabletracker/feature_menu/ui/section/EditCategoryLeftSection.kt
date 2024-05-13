package app.tabletracker.feature_menu.ui.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import app.tabletracker.feature_menu.data.entity.Category

@Composable
fun EditCategoryLeftSection(
    category: Category,
    update: Boolean = false,
    onCategoryChange: (Category) -> Unit,
    onUpsertClick: (Category) -> Unit,
    onDeleteClick: (Category) -> Unit
) {
    Scaffold(
        bottomBar = {
            Row(modifier = Modifier.padding(4.dp)) {
                Button(
                    onClick = { onUpsertClick(category) },
                    modifier = Modifier
                        .padding(if (update) 2.dp else 0.dp)
                        .fillMaxWidth()
                        .weight(if (update) .8f else 1f)
                ) {
                    Text(text = if (update) "Update" else "Create")
                }

                if (update) {
                    Button(
                        onClick = { onDeleteClick(category) },
                        modifier = Modifier.weight(.2f).padding(2.dp),
                        colors = ButtonDefaults.buttonColors().copy(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Menu Item"
                        )
                    }
                }
            }

        }
    ) {
        Column(
            modifier = Modifier.padding(it).padding(8.dp)
        ) {
            Text(
                text = if (update) "Update the category" else "Create a new category",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = category.name,
                onValueChange = { value ->
                    onCategoryChange(category.copy(name = value))
                },
                label = {
                    Text(text = "Category name")
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                singleLine = true
            )
        }
    }
}