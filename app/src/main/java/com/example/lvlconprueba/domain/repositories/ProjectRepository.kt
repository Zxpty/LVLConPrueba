package com.example.lvlconprueba.domain.repositories

import com.example.lvlconprueba.domain.model.Project
import com.example.lvlconprueba.domain.model.ProjectComboData
interface ProjectRepository {
    suspend fun getInitForm(): Result<ProjectComboData>
    suspend fun saveOrUpdateProject(project: Project): Result<Unit>
    suspend fun findProjects(
        start: Int? = null,
        sort: String? = null,
        limit: Int? = null,
        categoriaId: String? = null,
        estadoCodigo: String? = null,
        iconoCodigo: String? = null,
        nombre: String? = null,
        codigo: String? = null
    ): Result<List<Project>>
}
