package com.example.lvlconprueba.domain.usecase

import com.example.lvlconprueba.domain.model.Project
import com.example.lvlconprueba.domain.repositories.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FindProjectsUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(
        start: Int? = null,
        sort: String? = null,
        limit: Int? = null,
        categoriaId: String? = null,
        estadoCodigo: String? = null,
        iconoCodigo: String? = null,
        nombre: String? = null,
        codigo: String? = null
    ): Flow<Result<List<Project>>> = flow {
        emit(repository.findProjects(start, sort, limit, categoriaId, estadoCodigo, iconoCodigo, nombre, codigo))
    }
}
