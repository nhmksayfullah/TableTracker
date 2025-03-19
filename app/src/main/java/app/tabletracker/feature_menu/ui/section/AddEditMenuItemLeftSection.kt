package app.tabletracker.feature_menu.ui.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.tabletracker.feature_menu.data.entity.MenuItem
import app.tabletracker.feature_menu.data.entity.withUpdatedPrices
import app.tabletracker.feature_menu.ui.EditMenuUiEvent
import app.tabletracker.feature_order.data.entity.OrderType

@Composable
fun AddEditMenuItemLeftSection(
    menuItem: MenuItem,
    modifier: Modifier = Modifier,
    onEditMenu: (event: EditMenuUiEvent) -> Unit
) {
    var dineInPrice by remember {
        mutableStateOf(menuItem.prices[OrderType.DineIn].toString())
    }
    var takeOutPrice by remember {
        mutableStateOf(menuItem.prices[OrderType.TakeOut].toString())
    }
    var deliveryPrice by remember {
        mutableStateOf(menuItem.prices[OrderType.Delivery].toString())
    }
    LaunchedEffect(menuItem) {
        dineInPrice =
            if (menuItem.prices[OrderType.DineIn].toString() == "null") "" else menuItem.prices[OrderType.DineIn].toString()
        takeOutPrice =
            if (menuItem.prices[OrderType.TakeOut].toString() == "null") "" else menuItem.prices[OrderType.TakeOut].toString()
        deliveryPrice =
            if (menuItem.prices[OrderType.Delivery].toString() == "null") "" else menuItem.prices[OrderType.Delivery].toString()
    }

    Scaffold(
        bottomBar = {
            AddEditMenuItemBottomBar(
                showDelete = menuItem.name.isNotEmpty(),
                showUpdate = true,
                onUpsertClick = {
                    onEditMenu(
                        EditMenuUiEvent.ChangeDetailsOfMenuItem(
                            menuItem.withUpdatedPrices(
                                mapOf(
                                    Pair(OrderType.DineIn, dineInPrice.toFloatOrNull() ?: 0.0f),
                                    Pair(OrderType.TakeOut, takeOutPrice.toFloatOrNull() ?: 0.0f),
                                    Pair(OrderType.Delivery, deliveryPrice.toFloatOrNull() ?: 0.0f)
                                )
                            )
                        )
                    )
                    onEditMenu(EditMenuUiEvent.UpsertMenuItem(menuItem))
                },
                onDeleteClick = { onEditMenu(EditMenuUiEvent.DeleteMenuItem(menuItem)) }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            AddEditMenuItemBody(
                menuItem = menuItem,
                dineInPrice = dineInPrice,
                takeOutPrice = takeOutPrice,
                deliveryPrice = deliveryPrice,
                onDineInPriceChange = { dineInPrice = it },
                onTakeOutPriceChange = { takeOutPrice = it },
                onDeliveryPriceChange = { deliveryPrice = it },
                onEditMenu = onEditMenu
            )
        }
    }

}

@Composable
fun AddEditMenuItemBody(
    menuItem: MenuItem,
    dineInPrice: String,
    takeOutPrice: String,
    deliveryPrice: String,
    modifier: Modifier = Modifier,
    onDineInPriceChange: (String) -> Unit,
    onTakeOutPriceChange: (String) -> Unit,
    onDeliveryPriceChange: (String) -> Unit,
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
            },
            singleLine = true
        )

        TextField(
            value = menuItem.abbreviation,
            onValueChange = {
                onEditMenu(EditMenuUiEvent.ChangeDetailsOfMenuItem(menuItem.copy(abbreviation = it)))
            },
            label = {
                Text(text = "Item abbreviation")
            },
            singleLine = true
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
            value = dineInPrice,
            onValueChange = onDineInPriceChange,
            label = {
                Text(text = "Dine In Price")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        TextField(
            value = takeOutPrice,
            onValueChange = onTakeOutPriceChange,
            label = {
                Text(text = "Takeout Price")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        TextField(
            value = deliveryPrice,
            onValueChange = onDeliveryPriceChange,
            label = {
                Text(text = "Delivery Price")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
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
    Row(modifier = modifier.imePadding()) {
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