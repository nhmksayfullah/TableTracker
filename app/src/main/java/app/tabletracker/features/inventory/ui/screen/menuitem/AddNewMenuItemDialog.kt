package app.tabletracker.features.inventory.ui.screen.menuitem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import app.tabletracker.features.inventory.ui.components.ColorSelector
import app.tabletracker.features.inventory.util.DatabaseOperation
import app.tabletracker.features.inventory.data.entity.MenuItem
import app.tabletracker.features.order.data.entity.OrderType

@Composable
fun AddNewMenuItemDialog(
    menuItem: MenuItem,
    databaseOperation: DatabaseOperation,
    modifier: Modifier = Modifier,
    onValueChange: (MenuItem) -> Unit,
    onCreateClick: (newPrices: Map<OrderType, Float>) -> Unit,
    onDeleteClick: () -> Unit,
    onUpdatedClick: (newPrices: Map<OrderType, Float>) -> Unit,
) {
    var priceMap by remember {
        mutableStateOf(
            mapOf<OrderType, String>(
                OrderType.DineIn to "",
                OrderType.TakeOut to "",
                OrderType.Delivery to ""
            )
        )
    }

    LaunchedEffect(menuItem.prices) {
        if (menuItem.prices.isNotEmpty()) {
            priceMap = menuItem.prices.mapValues {
                it.value.toString()
            }
        }
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            Column {
                when (databaseOperation) {
                    DatabaseOperation.Add -> {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            onClick = {
                                onCreateClick(
                                    priceMap.mapValues {
                                        it.value.toFloatOrNull() ?: 0f
                                    }
                                )
                                priceMap = mapOf(
                                    OrderType.DineIn to "",
                                    OrderType.TakeOut to "",
                                    OrderType.Delivery to ""
                                )

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
                                    onUpdatedClick(
                                        priceMap.mapValues {
                                            it.value.toFloatOrNull() ?: 0f
                                        }
                                    )
                                    priceMap = mapOf(
                                        OrderType.DineIn to "",
                                        OrderType.TakeOut to "",
                                        OrderType.Delivery to ""
                                    )
                                }
                            ) {
                                Text(text = "Update")
                            }
                            Button(
                                modifier = Modifier
                                    .padding(start = 2.dp, end = 8.dp),
                                onClick = {
                                    onDeleteClick()
                                    priceMap = mapOf(
                                        OrderType.DineIn to "",
                                        OrderType.TakeOut to "",
                                        OrderType.Delivery to ""
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.onError,
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text(text = "Delete")
                            }
                        }
                    }

                    DatabaseOperation.Delete -> {}
                    DatabaseOperation.Read -> {}
                }


            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(8.dp)
        ) {
            TextField(
                value = menuItem.name,
                onValueChange = {
                    onValueChange(menuItem.copy(name = it))
                },
                label = {
                    Text(text = "Name")
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            TextField(
                value = menuItem.abbreviation,
                onValueChange = {
                    onValueChange(menuItem.copy(abbreviation = it))
                },
                label = {
                    Text(text = "Short Name")
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            priceMap.forEach { (orderType, price) ->
                TextField(
                    value = price,
                    onValueChange = {
                        priceMap = priceMap.toMutableMap().apply {
                            this[orderType] = it
                        }
                    },
                    label = {
                        Text(text = "${orderType.label} price")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ColorSelector(
                selectedColorArgb = menuItem.color,
                onColorSelected = { colorArgb ->
                    onValueChange(menuItem.copy(color = colorArgb))
                },
                modifier = Modifier.fillMaxWidth()
            )

        }
    }

}
