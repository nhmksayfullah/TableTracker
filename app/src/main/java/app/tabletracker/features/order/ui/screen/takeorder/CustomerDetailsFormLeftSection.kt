package app.tabletracker.features.order.ui.screen.takeorder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.tabletracker.features.customer.data.model.Customer


@Composable
fun CustomerDetailsFormLeftSection(
    customer: Customer?,
    modifier: Modifier = Modifier,
    onCustomerChange: (Customer) -> Unit
) {
    var customerName by rememberSaveable {
        mutableStateOf(customer?.name ?: "")
    }

    var customerContact by rememberSaveable {
        mutableStateOf(customer?.contact ?: "")
    }

    var customerPostCode by rememberSaveable {
        mutableStateOf(customer?.postCode ?: "")
    }

    var customerHouseNumber by rememberSaveable {
        mutableStateOf(customer?.houseNumber ?: "")
    }
    var customerStreet by rememberSaveable {
        mutableStateOf(customer?.street ?: "")
    }
    LaunchedEffect(customer) {
        customerName = customer?.name ?: ""
        customerContact = customer?.contact ?: ""
        customerPostCode = customer?.postCode ?: ""
        customerHouseNumber = customer?.houseNumber ?: ""
        customerStreet = customer?.street ?: ""
    }


    Column(
        modifier = modifier
    ) {
        TextField(
            value = customerName,
            onValueChange = {
                customerName = it
                if (customer != null) {
                    onCustomerChange(customer.copy(name = it))
                } else {
                    onCustomerChange(
                        Customer(
                            name = customerName,
                            contact = customerContact,
                            postCode = customerPostCode,
                            houseNumber = customerHouseNumber,
                            street = customerStreet
                        )
                    )
                }
            },
            label = {
                Text(text = "Name")
            },
            placeholder = {
                Text(text = "Jon Doe")
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = customerContact,
            onValueChange = {
                customerContact = it
                if (customer != null) {
                    onCustomerChange(customer.copy(contact = it))
                } else {
                    onCustomerChange(
                        Customer(
                            name = customerName,
                            contact = customerContact,
                            postCode = customerPostCode,
                            houseNumber = customerHouseNumber,
                            street = customerStreet
                        )
                    )
                }
            },
            label = {
                Text(text = "Contact Number")
            },
            placeholder = {
                Text(text = "07123456789")
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Phone
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = customerPostCode,
            onValueChange = {
                customerPostCode = it
                if (customer != null) {
                    onCustomerChange(customer.copy(postCode = it))
                } else {
                    onCustomerChange(
                        Customer(
                            name = customerName,
                            contact = customerContact,
                            postCode = customerPostCode,
                            houseNumber = customerHouseNumber,
                            street = customerStreet
                        )
                    )
                }
            },
            label = {
                Text(text = "Postcode")
            },
            placeholder = {
                Text(text = "M12 0LA")
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = customerHouseNumber,
            onValueChange = {
                customerHouseNumber = it
                if (customer != null) {
                    onCustomerChange(customer.copy(houseNumber = it))
                } else {
                    onCustomerChange(
                        Customer(
                            name = customerName,
                            contact = customerContact,
                            postCode = customerPostCode,
                            houseNumber = customerHouseNumber,
                            street = customerStreet
                        )
                    )
                }

            },
            label = {
                Text(text = "House Number")
            },
            placeholder = {
                Text(text = "13")
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = customerStreet,
            onValueChange = {
                customerStreet = it
                if (customer != null) {
                    onCustomerChange(customer.copy(street = it))
                } else {
                    onCustomerChange(
                        Customer(
                            name = customerName,
                            contact = customerContact,
                            postCode = customerPostCode,
                            houseNumber = customerHouseNumber,
                            street = customerStreet
                        )
                    )
                }
            },
            label = {
                Text(text = "Street")
            },
            placeholder = {
                Text(text = "ABC Lane")
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}