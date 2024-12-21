package app.tabletracker.feature_menu.ui.newsection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import app.tabletracker.feature_menu.data.entity.Category
import app.tabletracker.feature_menu.ui.EditMenuUiEvent

@Composable
fun AddEditCategoryLeftSection(
    category: Category,
    modifier: Modifier = Modifier,
    onEditMenu: (event: EditMenuUiEvent) -> Unit
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            EditCategoryBottomBar(
                showDelete = category.name.isNotEmpty(),
                showUpdate = true,
                onUpsertClick = { onEditMenu(EditMenuUiEvent.UpsertCategory(category)) },
                onDeleteClick = { onEditMenu(EditMenuUiEvent.DeleteCategory(category)) }
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            EditCategoryBody(
                category = category,
                onEditMenu = onEditMenu
            )
        }
    }
}

@Composable
fun EditCategoryBody(
    category: Category,
    modifier: Modifier = Modifier,
    onEditMenu: (event: EditMenuUiEvent) -> Unit
) {

    Column {
        TextField(
            value = category.name,
            onValueChange = {
                onEditMenu(EditMenuUiEvent.ChangeCategory(category.copy(name = it)))
            },
            label = {
                Text(text = "Category name")
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )

        Text(text = "Print to Kitchen Copy?")
        Switch(
            checked = category.isKitchenCategory,
            onCheckedChange = {
                onEditMenu(EditMenuUiEvent.ChangeCategory(category.copy(isKitchenCategory = it)))
            }
        )
    }

}

@Composable
fun EditCategoryBottomBar(
    modifier: Modifier = Modifier,
    showDelete: Boolean = false,
    showUpdate: Boolean = false,
    onUpsertClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}

) {
    Row(modifier = modifier.imePadding()) {
        Button(
            modifier = Modifier.weight(1f).padding(4.dp),
            onClick = onUpsertClick
        ) {
            if (showUpdate) Text(text = "Update") else Text(text = "Create")
        }

        if (showDelete) {
            Button(
                modifier = Modifier.weight(1f).padding(4.dp),
                onClick = onDeleteClick
            ) {
                Text(text = "Delete")
            }
        }
    }
}