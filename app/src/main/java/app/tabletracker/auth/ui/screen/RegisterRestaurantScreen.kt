package app.tabletracker.auth.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.tabletracker.auth.ui.AuthViewModel

@Composable
fun RegisterRestaurantScreen(
    authViewModel: AuthViewModel,
    onRegisterClick: () -> Unit
) {
    val authUiState by authViewModel.uiState.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(.7f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = authUiState.restaurant.name,
                onValueChange = {
                    authViewModel.updateUiState(authUiState.restaurant.copy(name = it))
                },
                label = {
                    Text(text = "Restaurant Name")
                },
                placeholder = {
                    Text(text = "Madras Spice Restaurant")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = authUiState.restaurant.address,
                onValueChange = {
                    authViewModel.updateUiState(authUiState.restaurant.copy(address = it))
                },
                label = {
                    Text(text = "Address")
                },
                placeholder = {
                    Text(text = "180 Northenden Rd, Sale M33 2SR")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = authUiState.restaurant.contactNumber,
                onValueChange = {
                    authViewModel.updateUiState(authUiState.restaurant.copy(contactNumber = it))
                },
                label = {
                    Text(text = "Contact Number")
                },
                placeholder = {
                    Text(text = "0712345678")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Phone
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            authUiState.restaurant.website?.let {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = it,
                    onValueChange = {
                        authViewModel.updateUiState(authUiState.restaurant.copy(website = it))
                    },
                    label = {
                        Text(text = "Website")
                    },
                    placeholder = {
                        Text(text = "www.madras-spice.uk")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = authUiState.restaurant.vatNumber,
                onValueChange = {
                    authViewModel.updateUiState(authUiState.restaurant.copy(vatNumber = it))
                },
                label = {
                    Text(text = "VAT Number")
                },
                placeholder = {
                    Text(text = "123456789")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onRegisterClick()
                }
            ) {
                Text(text = "Register")
            }
        }
    }
}