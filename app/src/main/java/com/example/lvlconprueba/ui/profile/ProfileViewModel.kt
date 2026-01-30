package com.example.lvlconprueba.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lvlconprueba.data.local.AuthManager
import com.example.lvlconprueba.domain.model.User
import com.example.lvlconprueba.domain.usecase.GetUserProfileUseCase
import com.example.lvlconprueba.domain.usecase.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val authManager: AuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(User())
    val formState: StateFlow<User> = _formState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            val currentUser = authManager.getCurrentUser()
            val usuarioId = currentUser?.usuarioId
            
            if (usuarioId != null) {
                getUserProfileUseCase(usuarioId).collect { result ->
                    result.onSuccess { user ->
                        val updatedUser = user.copy(
                            rolId = currentUser.rolCodigo ?: "",
                            usuario = currentUser.usuario ?: "",
                            empresa = user.empresa,
                            password = "",
                            url = user.url
                        )
                        _formState.value = updatedUser
                        _uiState.value = ProfileUiState.Success
                    }.onFailure { error ->
                        _uiState.value = ProfileUiState.Error(error.message ?: "Error al cargar perfil")
                    }
                }
            } else {
                _uiState.value = ProfileUiState.Error("No se encontró ID de usuario")
            }
        }
    }

    fun onFieldChange(
        nombre: String? = null,
        apellido: String? = null,
        telefono: String? = null,
        correo: String? = null
    ) {
        _formState.value = _formState.value.copy(
            nombre = nombre ?: _formState.value.nombre,
            apellido = apellido ?: _formState.value.apellido,
            telefono = telefono ?: _formState.value.telefono,
            correo = correo ?: _formState.value.correo
        )
    }

    fun updateProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Updating
            updateUserProfileUseCase(_formState.value).collect { result ->
                result.onSuccess {
                    _uiState.value = ProfileUiState.Updated("Perfil actualizado correctamente")
                }.onFailure { error ->
                    _uiState.value = ProfileUiState.Error(error.message ?: "Error al actualizar perfil")
                }
            }
        }
    }

    fun dismissMessage() {
        if (_uiState.value is ProfileUiState.Error || _uiState.value is ProfileUiState.Updated) {
            _uiState.value = ProfileUiState.Success
        }
    }
}

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    object Success : ProfileUiState()
    object Updating : ProfileUiState()
    data class Updated(val message: String) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}
