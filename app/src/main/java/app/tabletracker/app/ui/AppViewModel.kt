package app.tabletracker.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tabletracker.app.domain.repository.ApplicationRepository
import app.tabletracker.core.navigation.Applications
import app.tabletracker.core.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(private val repository: ApplicationRepository): ViewModel() {

    var uiState = MutableStateFlow(AppUiState())
        private set
    private var job: Job? = null


    init {
        checkRegistrationState()
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCategoryIndex()
        }
    }

    fun onEvent(appUiEvent: AppUiEvent) {
        when(appUiEvent) {
            is AppUiEvent.ChangeApplication -> {
                uiState.update {
                    it.copy(
                        currentApplication = appUiEvent.application
                    )
                }
            }

            is AppUiEvent.ChangeScreen -> {
                uiState.update {
                    it.copy(
                        currentScreen = appUiEvent.screen
                    )
                }
            }
        }
    }

    private fun checkRegistrationState() {
        repository.isUserRegistered().onEach {
            uiState.update {currentState ->
                currentState.copy(
                    isUserRegistered = it
                )
            }
            if (uiState.value.isUserRegistered) {
                checkApplicationState()
            } else {
                uiState.update {currentState ->
                    currentState.copy(
                        currentApplication = Applications.AuthenticationApp,
                        currentScreen = Screen.RegisterRestaurantScreen,
                        isLoading = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
    private fun checkApplicationState() {
        job?.cancel()
        job = repository.isTableNotEmpty().onEach {
            uiState.update {currentState ->
                currentState.copy(
                    readyToTakeOrder = it,
                    currentApplication = if (it) Applications.OrderManagementApp else Applications.MenuManagementApp,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }
}