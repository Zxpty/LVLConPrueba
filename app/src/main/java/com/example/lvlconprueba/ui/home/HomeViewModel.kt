package com.example.lvlconprueba.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lvlconprueba.domain.model.Project
import com.example.lvlconprueba.domain.model.ProjectComboData
import com.example.lvlconprueba.domain.usecase.FindProjectsUseCase
import com.example.lvlconprueba.domain.usecase.GetProjectInitFormUseCase
import com.example.lvlconprueba.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val findProjectsUseCase: FindProjectsUseCase,
    private val getProjectInitFormUseCase: GetProjectInitFormUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _user = MutableStateFlow<String?>(null)
    val user: StateFlow<String?> = _user.asStateFlow()

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole.asStateFlow()

    private val _comboData = MutableStateFlow<ProjectComboData?>(null)
    val comboData: StateFlow<ProjectComboData?> = _comboData.asStateFlow()

    private val _searchFilters = MutableStateFlow(SearchFilters())
    val searchFilters: StateFlow<SearchFilters> = _searchFilters.asStateFlow()

    init {
        loadProjectData()
        loadUserInfo()
        loadComboData()
    }

    private fun loadProjectData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val filters = _searchFilters.value
            findProjectsUseCase(
                start = 0,
                limit = 100,
                codigo = filters.codigo.ifBlank { "" },
                nombre = filters.nombre.ifBlank { "" },
                estadoCodigo = filters.estadoCodigo,
                categoriaId = filters.categoriaId,
                iconoCodigo = filters.iconoCodigo
            ).collect { result ->
                result.onSuccess { projects ->
                    _uiState.value = HomeUiState.Success(projects)
                }.onFailure { error ->
                    _uiState.value = HomeUiState.Error(error.message ?: "Error al cargar proyectos")
                }
            }
        }
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            try {
                val currentUser = loginUseCase.getCurrentUser()
                _user.value = currentUser?.nombre
                _userRole.value = currentUser?.rol
            } catch (e: Exception) {
            }
        }
    }

    private fun loadComboData() {
        viewModelScope.launch {
            getProjectInitFormUseCase().collect { result ->
                result.onSuccess { data ->
                    _comboData.value = data
                }
            }
        }
    }

    fun onSearchFiltersChange(filters: SearchFilters) {
        _searchFilters.value = filters
    }

    fun applySearch() {
        loadProjectData()
    }

    fun clearFilters() {
        _searchFilters.value = SearchFilters()
        loadProjectData()
    }

    fun retryLoadData() {
        loadProjectData()
    }
}

data class SearchFilters(
    val codigo: String = "",
    val nombre: String = "",
    val estadoCodigo: String? = null,
    val categoriaId: String? = null,
    val iconoCodigo: String? = null,
    val fechaInicio: String? = null,
    val fechaFin: String? = null
)

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val projects: List<Project>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
