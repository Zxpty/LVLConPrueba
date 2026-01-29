package com.example.lvlconprueba.data.repositories

import android.util.Log
import com.example.lvlconprueba.data.api.ProjectApiService
import com.example.lvlconprueba.data.dto.CreateProjectRequestDto
import com.example.lvlconprueba.data.dto.FindProjectsRequestDto
import com.example.lvlconprueba.data.mapper.toComboDomain
import com.example.lvlconprueba.data.mapper.toDomain
import com.example.lvlconprueba.domain.model.Project
import com.example.lvlconprueba.domain.model.ProjectComboData
import com.example.lvlconprueba.domain.repositories.ProjectRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepositoryImpl @Inject constructor(
    private val apiService: ProjectApiService
) : ProjectRepository {

    override suspend fun getInitForm(): Result<ProjectComboData> {
        return try {
            val response = apiService.getInitForm()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(dto.toComboDomain())
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveOrUpdateProject(project: Project): Result<Unit> {
        return try {
            val request = CreateProjectRequestDto(
                proyectoId = project.proyectoId,
                categoriaId = project.categoriaId,
                usuarioId = project.usuarioId,
                codigo = project.codigo,
                nombre = project.nombre,
                descripcion = project.descripcion,
                estadoCodigo = project.estadoCodigo,
                iconoCodigo = project.iconoCodigo,
                fechaInicio = project.fechaInicio,
                fechaFin = project.fechaFin,
                compartir = project.compartir == true
            )
            val response = apiService.saveOrUpdateProject(request)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                // para debuguear el mensaje de status
                Log.d("ProjectRepositoryImpl", "Response: codigo=${dto.codigo}, mensaje=${dto.mensaje}")
                if (dto.codigo == "200" || dto.codigo == "201" || dto.mensaje?.contains("correctamente", ignoreCase = true) == true) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(dto.mensaje ?: "Error al guardar el proyecto"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun findProjects(
        start: Int?,
        sort: String?,
        limit: Int?,
        categoriaId: String?,
        estadoCodigo: String?,
        iconoCodigo: String?,
        nombre: String?,
        codigo: String?
    ): Result<List<Project>> {
        return try {
            val request = FindProjectsRequestDto(
                start = start,
                sort = sort,
                limit = limit,
                categoriaId = categoriaId,
                estadoCodigo = estadoCodigo,
                iconoCodigo = iconoCodigo,
                nombre = nombre,
                codigo = codigo
            )
            val response = apiService.findProjects(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
