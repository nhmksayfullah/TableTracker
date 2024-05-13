package app.tabletracker.auth.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.tabletracker.auth.ui.AuthViewModel

@Composable
fun RegisterLicenceScreen(
    authViewModel: AuthViewModel,
    onRegistrationSuccessful: () -> Unit
) {
    val authUiState by authViewModel.uiState.collectAsState()
    Log.d("check: ", authUiState.restaurant.toString())
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxHeight().fillMaxWidth(.7f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = authUiState.restaurant.licence,
                onValueChange = {
                    authViewModel.updateUiState(authUiState.restaurant.copy(licence = it))
                },
                label = {
                    Text(text = "Licence")
                },
                placeholder = {
                    Text(text = "xxxxxxxx-xxxx-xxxxx-xxxxx-xxxxxxxxxxxx")
                },
                singleLine = true,
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val registered = authViewModel.registerRestaurant()
                    if (registered) {
                        onRegistrationSuccessful()
                    }
                }
            ) {
                Text(text = "Register")
            }
        }
    }
}