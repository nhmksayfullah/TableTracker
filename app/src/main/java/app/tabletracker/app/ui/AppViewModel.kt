package app.tabletracker.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tabletracker.app.domain.repository.ApplicationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class AppViewModel(
    private val repository: ApplicationRepository
) : ViewModel() {

    var uiState = MutableStateFlow(AppUiState())
        private set


    init {
        checkRegistrationStatus()
    }


    private fun checkRegistrationStatus() {
        repository.isUserRegistered().onEach { registrationStatus ->
            uiState.update { currentState ->
                currentState.copy(
                    isRegistered = registrationStatus
                )
            }
            if (registrationStatus) {
                checkApplicationState()
            } else {
                uiState.update { currentState ->
                    currentState.copy(
                        loading = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun checkApplicationState() {
        repository.hasInventory().onEach { hasInventory ->
            uiState.update { currentState ->
                currentState.copy(
                    hasInventory = hasInventory,
                    loading = false
                )
            }
        }.launchIn(viewModelScope)
    }
}