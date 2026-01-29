package com.example.lvlconprueba.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lvlconprueba.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            delay(2000)
            val isLoggedIn = loginUseCase.isLoggedIn()
            _uiState.value = if (isLoggedIn) {
                SplashUiState.Authenticated
            } else {
                SplashUiState.NotAuthenticated
            }
        }
    }
}

sealed class SplashUiState {
    object Loading : SplashUiState()
    object Authenticated : SplashUiState()
    object NotAuthenticated : SplashUiState()
}
