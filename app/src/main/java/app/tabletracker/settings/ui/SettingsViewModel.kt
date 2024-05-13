package app.tabletracker.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tabletracker.common.data.RestaurantExtra
import app.tabletracker.settings.domain.SettingsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepository): ViewModel() {
    private var _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()


    private var populateRestaurantInfoJob: Job? = null
    private var populateRestaurantExtraJob: Job? = null

    init {
        populateRestaurantInfo()
    }

    fun onEvent(event: SettingsUiEvent) {
        when(event) {
            is SettingsUiEvent.UpdateRestaurantExtra -> {
                updateRestaurantExtra(event.restaurantExtra)
            }
        }

    }

    private fun updateRestaurantExtra(restaurantExtra: RestaurantExtra) {
        viewModelScope.launch {
            repository.upsertRestaurantExtra(restaurantExtra)
        }
    }

    private fun populateRestaurantInfo() {
        populateRestaurantInfoJob?.cancel()
        populateRestaurantInfoJob = repository.readRestaurantInfo().onEach {
            _uiState.update {currentState ->
                currentState.copy(
                    restaurantInfo = it
                )
            }
            populateRestaurantExtra(it.id)
        }.launchIn(viewModelScope)
    }

    private fun populateRestaurantExtra(restaurantId: String) {
        if (uiState.value.restaurantInfo != null) {
            if (uiState.value.restaurantInfo!!.id.isNotEmpty()) {
                populateRestaurantExtraJob?.cancel()
                populateRestaurantExtraJob = repository.readRestaurantExtra(restaurantId).onEach {
                    _uiState.update {state ->
                        state.copy(
                            restaurantExtra = it
                        )
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

}