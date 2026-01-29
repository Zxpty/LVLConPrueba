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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProjectViewModel @Inject constructor(
    private val createProjectUseCase: CreateProjectUseCase,
    private val getProjectInitFormUseCase: GetProjectInitFormUseCase,
    private val authManager: AuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreateProjectUiState>(CreateProjectUiState.Loading)
    val uiState: StateFlow<CreateProjectUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(CreateProjectFormState())
    val formState: StateFlow<CreateProjectFormState> = _formState.asStateFlow()

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

    init {
        loadComboData()
        loadCurrentUserId()
    }

    private fun loadCurrentUserId() {
        viewModelScope.launch {
            try {
                val user = authManager.getCurrentUser()
                _currentUserId.value = user?.usuarioId
                Log.d("CreateProjectViewModel", "Current user ID: ${user?.usuarioId}")
            } catch (e: Exception) {
                Log.e("CreateProjectViewModel", "Error loading user ID: ${e.message}")
            }
        }
    }

    private fun loadComboData() {
        viewModelScope.launch {
            _uiState.value = CreateProjectUiState.Loading
            getProjectInitFormUseCase().collect { result ->
                result.onSuccess { comboData ->
                    _uiState.value = CreateProjectUiState.Success(comboData)
                    if (comboData.iconos.isNotEmpty() && _formState.value.iconoCodigo == null) {
                        _formState.value = _formState.value.copy(iconoCodigo = comboData.iconos.first().codigo)
                    }
                    if (comboData.categorias.isNotEmpty() && _formState.value.categoriaId == null) {
                        _formState.value = _formState.value.copy(categoriaId = comboData.categorias.first().id)
                    }
                    if (comboData.estados.isNotEmpty() && _formState.value.estadoCodigo == null) {
                        _formState.value = _formState.value.copy(estadoCodigo = comboData.estados.first().codigo)
                    }
                }.onFailure { error ->
                    _uiState.value = CreateProjectUiState.Error(error.message ?: "Error al cargar datos del formulario")
                }
            }
        }
    }

    fun onNombreChange(nombre: String) {
        _formState.value = _formState.value.copy(nombre = nombre)
    }

    fun onDescripcionChange(descripcion: String) {
        _formState.value = _formState.value.copy(descripcion = descripcion)
    }

    fun onFechaInicioChange(fechaInicio: String) {
        _formState.value = _formState.value.copy(fechaInicio = fechaInicio)
    }

    fun onFechaFinChange(fechaFin: String) {
        _formState.value = _formState.value.copy(fechaFin = fechaFin)
    }

    fun onCategoriaChange(categoria: Categoria?) {
        _formState.value = _formState.value.copy(categoriaId = categoria?.id)
    }

    fun onEstadoChange(estado: Estado?) {
        _formState.value = _formState.value.copy(estadoCodigo = estado?.codigo)
    }

    fun onIconoChange(icono: Icono?) {
        _formState.value = _formState.value.copy(iconoCodigo = icono?.codigo)
    }

    fun nextIcono(iconos: List<Icono>) {
        if (iconos.isEmpty()) return
        val currentCodigo = _formState.value.iconoCodigo
        val currentIndex = iconos.indexOfFirst { it.codigo == currentCodigo }
        val nextIndex = (currentIndex + 1) % iconos.size
        _formState.value = _formState.value.copy(iconoCodigo = iconos[nextIndex].codigo)
    }

    fun onCompartirChange(compartir: Boolean) {
        _formState.value = _formState.value.copy(compartir = compartir)
    }

    fun createProject(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = CreateProjectUiState.Creating
            Log.d("CreateProjectViewModel", "Form state: nombre=${_formState.value.nombre}, descripcion=${_formState.value.descripcion}, categoriaId=${_formState.value.categoriaId}, estadoCodigo=${_formState.value.estadoCodigo}, iconoCodigo=${_formState.value.iconoCodigo}, compartir=${_formState.value.compartir}")
            val proyectoId = ""
            val project = com.example.lvlconprueba.domain.model.Project(
                proyectoId = proyectoId,
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
            Log.d("CreateProjectViewModel", "Project to send: proyectoId=${project.proyectoId}, categoriaId=${project.categoriaId}, usuarioId=${project.usuarioId}, nombre=${project.nombre}, codigo=${project.codigo}")

            createProjectUseCase(project).collect { result ->
                result.onSuccess {
                    _uiState.value = CreateProjectUiState.Created
                    onSuccess()
                }.onFailure { error ->
                    _uiState.value = CreateProjectUiState.Error(error.message ?: "Error al crear el proyecto")
                    Log.e("CreateProjectViewModel", "Error creating project: ${error.message}")
                }
            }
        }
    }

    fun dismissError() {
        val currentState = _uiState.value
        if (currentState is CreateProjectUiState.Error) {
            _uiState.value = CreateProjectUiState.Idle
        }
    }
}

sealed class CreateProjectUiState {
    object Loading : CreateProjectUiState()
    object Idle : CreateProjectUiState()
    data class Success(val comboData: ProjectComboData) : CreateProjectUiState()
    object Creating : CreateProjectUiState()
    object Created : CreateProjectUiState()
    data class Error(val message: String) : CreateProjectUiState()
}

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
