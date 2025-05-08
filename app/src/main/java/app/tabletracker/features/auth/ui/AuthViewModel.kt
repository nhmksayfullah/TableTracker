package app.tabletracker.features.auth.ui

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tabletracker.app.MADRAS_SPICE_RESTAURANT
import app.tabletracker.features.auth.data.model.DeviceType
import app.tabletracker.features.auth.data.model.Restaurant
import app.tabletracker.features.auth.data.repository.DevicePreferencesRepository
import app.tabletracker.features.auth.domain.repository.AuthRepository
import app.tabletracker.core.model.RestaurantExtra
import app.tabletracker.features.companion.client.SocketClientManagerImpl
import app.tabletracker.features.companion.model.ACTION_SYNC_RESTAURANT_INFO_STATUS
import app.tabletracker.features.companion.model.ClientRequest
import app.tabletracker.features.companion.model.EXTRA_SYNC_MESSAGE
import app.tabletracker.features.companion.model.EXTRA_SYNC_STATUS
import app.tabletracker.features.companion.model.SYNC_STATUS_FAILED
import app.tabletracker.features.companion.model.ServerResponse
import app.tabletracker.features.inventory.domain.repository.EditMenuRepository
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
import kotlinx.coroutines.flow.catch
import kotlinx.serialization.json.Json

class AuthViewModel(
    private val authRepo: AuthRepository,
    private val editMenuRepository: EditMenuRepository,
    private val deviceTypeRepo: DevicePreferencesRepository
) : ViewModel() {
    private val clientManager = SocketClientManagerImpl(
        onResponseReceived = ::handleServerResponse
    )



    private var _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    init {
        readRestaurantInfo()
        hasInventory()
    }

    fun syncRestaurantInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                clientManager.transmitDataToServer(
                    Json.encodeToString(ClientRequest.serializer(),
                        ClientRequest.SetupRestaurant
                    )
                )
            } catch (e: Exception) {
                Log.e("AuthViewModel", "syncRestaurantInfo: ", e)
            }
        }
    }

    fun syncMenu() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                clientManager.transmitDataToServer(
                    Json.encodeToString(ClientRequest.serializer(),
                        ClientRequest.SyncMenu
                    )
                )
            } catch (e: Exception) {
                Log.e("AuthViewModel", "syncMenu: ", e)
            }
        }
    }

    fun connectToServer(ipAddress: String, port: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                clientManager.connectToServer(ipAddress, port)
                clientManager.transmitDataToServer("Hello from client")
            } catch (e: Exception) {
                Log.e("AuthViewModel", "connectToServer: ", e)
            }
        }
    }

    private fun handleServerResponse(response: ServerResponse) {
        when(response) {
            is ServerResponse.Menu -> {
                viewModelScope.launch {
                    try {
                        response.menu.forEach { categoryWithMenuItems ->
                            editMenuRepository.writeCategory(categoryWithMenuItems.category)
                            categoryWithMenuItems.menuItems.forEach { menuItem ->
                                editMenuRepository.writeMenuItem(menuItem)
                            }
                        }
                        _uiState.update { currentState ->
                            currentState.copy(
                                hasInventory = true
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("AuthViewModel", "handleServerResponse: ", e)
                    }
                }
            }
            is ServerResponse.OrderInfo -> {}
            is ServerResponse.RestaurantInfo -> {
                viewModelScope.launch {
                    try {
                        val restaurant = response.restaurant
                        val restaurantExtra = response.restaurantExtra
                        authRepo.registerRestaurant(restaurant)
                        authRepo.upsertRestaurantExtra(restaurantExtra)
                        _uiState.update {
                            it.copy(
                                restaurant = restaurant,
                                restaurantExtra = restaurantExtra
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("AuthViewModel", "handleServerResponse: ", e)
                    }
                }
            }
        }
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

fun readRestaurantInfo() {
    try {
        authRepo.readRestaurantInfo().onEach { restaurant ->
            authRepo.readRestaurantExtra(restaurant.id).onEach { restaurantExtra ->
                _uiState.update { currentState ->
                    currentState.copy(
                        restaurant = restaurant,
                        restaurantExtra = restaurantExtra
                    )
                }
            }.launchIn(viewModelScope)
        }.catch { e ->
            // Handle the case when no restaurant exists
            if (e is IllegalStateException && e.message?.contains("query result was empty") == true) {
                _uiState.update { currentState ->
                    currentState.copy(
                        restaurant = null,
                        restaurantExtra = null
                    )
                }
            } else {
                // Handle other errors if needed
                e.printStackTrace()
            }
        }.launchIn(viewModelScope)
    } catch (e: Exception) {
        e.printStackTrace()
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