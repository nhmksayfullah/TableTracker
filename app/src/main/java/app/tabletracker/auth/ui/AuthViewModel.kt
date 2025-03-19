package app.tabletracker.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tabletracker.app.MADRAS_SPICE_RESTAURANT
import app.tabletracker.auth.data.model.Restaurant
import app.tabletracker.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private var _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun registerRestaurant(): Boolean {
        return if (uiState.value.restaurant.licence == MADRAS_SPICE_RESTAURANT.licence) {
            viewModelScope.launch {
                repository.registerRestaurant(uiState.value.restaurant)
            }
            true
        } else false
    }

    fun updateUiState(restaurant: Restaurant) {
        _uiState.update {
            it.copy(restaurant = restaurant)
        }
    }

    fun readRestaurantInfo() {
        repository.readRestaurantInfo().onEach {
            _uiState.update { currentState ->
                currentState.copy(
                    restaurant = it
                )
            }
        }.launchIn(viewModelScope)
    }
}

data class AuthUiState(
    val restaurant: Restaurant = Restaurant(
        name = "",
        address = "",
        contactNumber = "",
        licence = "",
        vatNumber = "",
        website = ""
    )
)