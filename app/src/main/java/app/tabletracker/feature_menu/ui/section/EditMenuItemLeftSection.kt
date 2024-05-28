package app.tabletracker.feature_menu.ui.section

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.tabletracker.core.ui.TabbedScreen
import app.tabletracker.feature_menu.data.entity.CategoryWithMenuItems
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_menu.util.EditMenuTabOption
import app.tabletracker.feature_order.data.entity.OrderType

@Composable
fun EditMenuItemLeftSection(
    menus: List<CategoryWithMenuItems>,
    menuItem: MenuItem,
    update: Boolean = false,
    onMenuItemDetailsChange: (MenuItem) -> Unit,
    onMenuItemPricesChange: (Pair<OrderType, Float>) -> Unit,
    onUpsertClick: (MenuItem) -> Unit,
    onDeleteClick: (MenuItem) -> Unit
) {
    Scaffold(
        bottomBar = {
            Row(modifier = Modifier.padding(4.dp)) {
                Button(
                    onClick = { onUpsertClick(menuItem) },
                    modifier = Modifier
                        .padding(if (update) 2.dp else 0.dp)
                        .fillMaxWidth()
                        .weight(if (update) .8f else 1f)
                ) {
                    Text(text = if (update) "Update" else "Create")
                }

                if (update) {
                    Button(
                        onClick = { onDeleteClick(menuItem) },
                        modifier = Modifier
                            .weight(.2f)
                            .padding(2.dp),
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
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                text = if (update) "Update the item" else "Add a new Item",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            TabbedScreen(
                titles = listOf(
                    EditMenuTabOption.Details.name,
                    EditMenuTabOption.Prices.name,
                    EditMenuTabOption.Advance.name
                )
            ) { state ->
                when (state) {
                    EditMenuTabOption.Details.state -> {
                        EditDetailsSection(menuItem = menuItem) { item ->
                            onMenuItemDetailsChange(item)
                        }
                    }

                    EditMenuTabOption.Prices.state -> {
                        EditPricesSection(menuItem = menuItem) { orderTypeFloatPair ->
                            onMenuItemPricesChange(orderTypeFloatPair)
                        }
                    }
                    EditMenuTabOption.Advance.state -> {
                        EditMenuItemAdvanceSection(
                            menus = menus,
                            menuItem = menuItem
                        ) {
                            onMenuItemDetailsChange(it)
                        }
                    }

                }

            }
        }
    }
}

@Composable
fun EditDetailsSection(
    menuItem: MenuItem,
    onMenuItemChange: (MenuItem) -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = menuItem.name,
            onValueChange = {
                onMenuItemChange(menuItem.copy(name = it))
            },
            label = {
                Text(text = "Name")
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = menuItem.abbreviation,
            onValueChange = {
                onMenuItemChange(menuItem.copy(abbreviation = it))
            },
            label = {
                Text(text = "Abbreviation")
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = menuItem.description,
            onValueChange = {
                onMenuItemChange(menuItem.copy(description = it))
            },
            label = {
                Text(text = "Description")
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )

    }
}

@Composable
fun EditPricesSection(
    menuItem: MenuItem,
    onMenuItemPricesChange: (Pair<OrderType, Float>) -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = menuItem.prices[OrderType.DineIn]?.takeIf { it != 0.0f }?.toString() ?: "",
            onValueChange = { value ->
                try {
                    onMenuItemPricesChange(Pair(OrderType.DineIn, value.toFloat()))
                } catch (_: Exception) {}
            },
            label = {
                Text(text = "${OrderType.DineIn.name} price")
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Decimal
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = menuItem.prices[OrderType.TakeOut]?.takeIf { it != 0.0f }?.toString() ?: "",
            onValueChange = { value ->
                try {
                    onMenuItemPricesChange(Pair(OrderType.TakeOut, value.toFloat()))
                } catch (_: Exception) {}
            },
            label = {
                Text(text = "${OrderType.TakeOut.name} price")
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Decimal
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = menuItem.prices[OrderType.Delivery]?.takeIf { it != 0.0f }?.toString() ?: "",
            onValueChange = { value ->
                try {
                    onMenuItemPricesChange(Pair(OrderType.Delivery, value.toFloat()))
                } catch (_: Exception) {}
            },
            label = {
                Text(text = "${OrderType.Delivery.name} price")
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            )
        )

    }
}