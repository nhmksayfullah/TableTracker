package app.tabletracker.auth.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tabletracker.app.MADRAS_SPICE_RESTAURANT
import app.tabletracker.auth.data.model.DeviceType
import app.tabletracker.auth.data.model.Restaurant
import app.tabletracker.auth.data.repository.DevicePreferencesRepository
import app.tabletracker.auth.domain.repository.AuthRepository
import app.tabletracker.common.data.RestaurantExtra
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepo: AuthRepository,
    private val deviceTypeRepo: DevicePreferencesRepository
) : ViewModel() {
    private var _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    val deviceType: StateFlow<DeviceType> = deviceTypeRepo.deviceType.map {
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DeviceType.Companion
    )

    init {
        readRestaurantInfo()
        hasInventory()
    }

    fun registerRestaurant(licence: String): Boolean {
        return if (licence == MADRAS_SPICE_RESTAURANT.licence) {
            val restaurant = uiState.value.restaurant?.copy(licence = licence)
            restaurant?.let {
                viewModelScope.launch(Dispatchers.IO) {
                    authRepo.registerRestaurant(it)
                }
            }
            true
        } else false
    }

    fun updateUiState(restaurant: Restaurant) {
        _uiState.update {
            it.copy(restaurant = restaurant)
        }
    }
    private fun hasInventory() {
        authRepo.hasInventory().onEach {
            try {
                it.let {
                    _uiState.update { currentState ->
                        currentState.copy(
                            hasInventory = it
                        )
                    }
                }
            } catch (e: Exception) {}
        }.launchIn(viewModelScope)
    }

    private fun readRestaurantInfo() {
        try {
            authRepo.readRestaurantInfo().onEach { restaurant ->
                if(restaurant != null) {
                    authRepo.readRestaurantExtra(restaurant.id).onEach { restaurantExtra ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                restaurant = restaurant,
                                restaurantExtra = restaurantExtra
                            )
                        }
                    }.launchIn(viewModelScope)
                }

            }.launchIn(viewModelScope)
        } catch (e: Exception) {
        }
    }

    fun updateDeviceType(deviceType: DeviceType) {
        viewModelScope.launch {
            deviceTypeRepo.saveDeviceType(deviceType)
        }
    }
}

data class AuthUiState(
    val restaurant: Restaurant? = null,
    val restaurantExtra: RestaurantExtra? = null,
    val hasInventory: Boolean = false
)