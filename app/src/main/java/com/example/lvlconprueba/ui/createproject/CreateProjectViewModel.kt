package com.example.lvlconprueba.ui.createproject

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lvlconprueba.data.local.AuthManager
import com.example.lvlconprueba.domain.model.Categoria
import com.example.lvlconprueba.domain.model.Estado
import com.example.lvlconprueba.domain.model.Icono
import com.example.lvlconprueba.domain.model.ProjectComboData
import com.example.lvlconprueba.domain.usecase.CreateProjectUseCase
import com.example.lvlconprueba.domain.usecase.GetProjectInitFormUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProjectViewModel @Inject constructor(
    private val createProjectUseCase: CreateProjectUseCase,
    private val getProjectInitFormUseCase: GetProjectInitFormUseCase,
    private val authManager: AuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateProjectUiState())
    val uiState: StateFlow<CreateProjectUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(CreateProjectFormState())
    val formState: StateFlow<CreateProjectFormState> = _formState.asStateFlow()

    private val _currentUserId = MutableStateFlow<String?>(null)

    init {
        loadComboData()
        loadCurrentUserId()
    }

    private fun loadCurrentUserId() {
        viewModelScope.launch {
            try {
                val user = authManager.getCurrentUser()
                _currentUserId.value = user?.usuarioId
            } catch (e: Exception) {
                Log.e("CreateProjectViewModel", "Error loading user ID: ${e.message}")
            }
        }
    }

    fun loadComboData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getProjectInitFormUseCase().collect { result ->
                result.onSuccess { comboData ->
                    _uiState.update { it.copy(isLoading = false, comboData = comboData) }
                    _formState.update { state ->
                        state.copy(
                            iconoCodigo = state.iconoCodigo ?: comboData.iconos.firstOrNull()?.codigo,
                            categoriaId = state.categoriaId ?: comboData.categorias.firstOrNull()?.id,
                            estadoCodigo = state.estadoCodigo ?: comboData.estados.firstOrNull()?.codigo
                        )
                    }
                }.onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message ?: "Error al cargar datos") }
                }
            }
        }
    }

    fun onNombreChange(nombre: String) { _formState.update { it.copy(nombre = nombre) } }
    fun onDescripcionChange(descripcion: String) { _formState.update { it.copy(descripcion = descripcion) } }
    fun onFechaInicioChange(fechaInicio: String) { _formState.update { it.copy(fechaInicio = fechaInicio) } }
    fun onFechaFinChange(fechaFin: String) { _formState.update { it.copy(fechaFin = fechaFin) } }
    fun onCategoriaChange(categoria: Categoria?) { _formState.update { it.copy(categoriaId = categoria?.id) } }
    fun onEstadoChange(estado: Estado?) { _formState.update { it.copy(estadoCodigo = estado?.codigo) } }
    fun onIconoChange(icono: Icono?) { _formState.update { it.copy(iconoCodigo = icono?.codigo) } }
    fun onCompartirChange(compartir: Boolean) { _formState.update { it.copy(compartir = compartir) } }

    fun createProject(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCreating = true, errorMessage = null) }
            val project = com.example.lvlconprueba.domain.model.Project(
                proyectoId = "",
                categoriaId = _formState.value.categoriaId,
                usuarioId = _currentUserId.value,
                codigo = "",
                nombre = _formState.value.nombre,
                descripcion = _formState.value.descripcion,
                estadoCodigo = _formState.value.estadoCodigo,
                iconoCodigo = _formState.value.iconoCodigo,
                fechaInicio = _formState.value.fechaInicio,
                fechaFin = _formState.value.fechaFin,
                compartir = _formState.value.compartir
            )

            createProjectUseCase(project).collect { result ->
                result.onSuccess {
                    _uiState.update { it.copy(isCreating = false, isCreated = true) }
                    onSuccess()
                    resetForm()
                }.onFailure { error ->
                    _uiState.update { it.copy(isCreating = false, errorMessage = error.message ?: "Error al crear el proyecto") }
                }
            }
        }
    }

    fun resetForm() {
        _formState.value = CreateProjectFormState()
        _uiState.update { state -> 
            state.copy(isCreated = false, errorMessage = null) 
        }
        // Restore defaults from existing comboData if available
        _uiState.value.comboData?.let { comboData ->
            _formState.update { state ->
                state.copy(
                    iconoCodigo = comboData.iconos.firstOrNull()?.codigo,
                    categoriaId = comboData.categorias.firstOrNull()?.id,
                    estadoCodigo = comboData.estados.firstOrNull()?.codigo
                )
            }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
        if (_uiState.value.comboData == null) {
            loadComboData()
        }
    }
}

data class CreateProjectUiState(
    val isLoading: Boolean = false,
    val isCreating: Boolean = false,
    val isCreated: Boolean = false,
    val comboData: ProjectComboData? = null,
    val errorMessage: String? = null
)

data class CreateProjectFormState(
    val nombre: String = "",
    val descripcion: String = "",
    val fechaInicio: String = "",
    val fechaFin: String = "",
    val categoriaId: String? = null,
    val estadoCodigo: String? = null,
    val iconoCodigo: String? = null,
    val compartir: Boolean = false
)
