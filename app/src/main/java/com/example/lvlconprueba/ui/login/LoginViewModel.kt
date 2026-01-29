package com.example.lvlconprueba.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lvlconprueba.domain.model.User
import com.example.lvlconprueba.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _usuario = MutableStateFlow("")
    val usuario: StateFlow<String> = _usuario.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun onUsuarioChange(newValue: String) {
        _usuario.value = newValue
    }

    fun onPasswordChange(newValue: String) {
        _password.value = newValue
    }

    fun login() {
        if (_usuario.value.isBlank() || _password.value.isBlank()) {
            _uiState.value = LoginUiState.Error("Por favor, ingrese usuario y contraseña")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            loginUseCase(_usuario.value, _password.value).collect { result ->
                result.onSuccess { user ->
                    loginUseCase.saveSession(user)
                    _uiState.value = LoginUiState.Success(user)
                }.onFailure { error ->
                    _uiState.value = LoginUiState.Error(error.message ?: "Ocurrió un error inesperado")
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = LoginUiState.Idle
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val user: User) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
