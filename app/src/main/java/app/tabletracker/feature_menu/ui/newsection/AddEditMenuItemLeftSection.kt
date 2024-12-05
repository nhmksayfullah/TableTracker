package app.tabletracker.feature_menu.ui.newsection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_menu.data.entity.withUpdatedPrice
import app.tabletracker.feature_menu.ui.EditMenuUiEvent
import app.tabletracker.feature_order.data.entity.OrderType

@Composable
fun AddEditMenuItemLeftSection(
    menuItem: MenuItem,
    modifier: Modifier = Modifier,
    onEditMenu: (event: EditMenuUiEvent) -> Unit
) {

    Scaffold(
        bottomBar = {
            AddEditMenuItemBottomBar(
                showDelete = menuItem.name.isNotEmpty(),
                showUpdate = true,
                onUpsertClick = { onEditMenu(EditMenuUiEvent.UpsertMenuItem(menuItem)) },
                onDeleteClick = { onEditMenu(EditMenuUiEvent.DeleteMenuItem(menuItem)) }
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            AddEditMenuItemBody(
                menuItem = menuItem,
                onEditMenu = onEditMenu
            )
        }
    }

}

@Composable
fun AddEditMenuItemBody(
    menuItem: MenuItem,
    modifier: Modifier = Modifier,
    onEditMenu: (event: EditMenuUiEvent) -> Unit
) {
    Column {
        TextField(
            value = menuItem.name,
            onValueChange = {
                onEditMenu(EditMenuUiEvent.ChangeDetailsOfMenuItem(menuItem.copy(name = it)))
            },
            label = {
                Text(text = "Item name")
            }
        )

        TextField(
            value = menuItem.abbreviation,
            onValueChange = {
                onEditMenu(EditMenuUiEvent.ChangeDetailsOfMenuItem(menuItem.copy(abbreviation = it)))
            },
            label = {
                Text(text = "Item abbreviation")
            }
        )
        TextField(
            value = menuItem.description,
            onValueChange = {
                onEditMenu(EditMenuUiEvent.ChangeDetailsOfMenuItem(menuItem.copy(description = it)))
            },
            label = {
                Text(text = "Item description")
            }
        )

        Spacer(Modifier.height(8.dp))

        TextField(
            value = menuItem.prices[OrderType.DineIn].toString(),
            onValueChange = {
                onEditMenu(
                    EditMenuUiEvent.ChangeDetailsOfMenuItem(
                        menuItem.withUpdatedPrice(OrderType.DineIn, it.toFloatOrNull()?: 0.0f)
                    )
                )
            },
            label = {
                Text(text = "Dine In Price")
            }
        )

        TextField(
            value = menuItem.prices[OrderType.TakeOut].toString(),
            onValueChange = {
                onEditMenu(
                    EditMenuUiEvent.ChangeDetailsOfMenuItem(
                        menuItem.withUpdatedPrice(OrderType.TakeOut, it.toFloatOrNull()?: 0.0f)
                    )
                )
            },
            label = {
                Text(text = "Dine In Price")
            }
        )

        TextField(
            value = menuItem.prices[OrderType.Delivery].toString(),
            onValueChange = {
                onEditMenu(
                    EditMenuUiEvent.ChangeDetailsOfMenuItem(
                        menuItem.withUpdatedPrice(OrderType.Delivery, it.toFloatOrNull()?: 0.0f)
                    )
                )
            },
            label = {
                Text(text = "Dine In Price")
            }
        )
    }
}

@Composable
fun AddEditMenuItemBottomBar(
    modifier: Modifier = Modifier,
    showDelete: Boolean = false,
    showUpdate: Boolean = false,
    onUpsertClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Row(modifier = modifier) {
        Button(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            onClick = onUpsertClick
        ) {
            if (showUpdate) Text(text = "Update") else Text(text = "Create")
        }

        if (showDelete) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                onClick = onDeleteClick
            ) {
                Text(text = "Delete")
            }
        }
    }
}