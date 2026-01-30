package com.example.lvlconprueba.data.repositories

import android.util.Log
import com.example.lvlconprueba.data.api.UserApiService
import com.example.lvlconprueba.data.dto.UpdateUserRequestDto
import com.example.lvlconprueba.data.mapper.toDomain
import com.example.lvlconprueba.domain.model.User
import com.example.lvlconprueba.domain.repositories.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: UserApiService
) : UserRepository {

    override suspend fun getUserProfile(usuarioId: String): Result<User> {
        return try {
            val response = apiService.getUserById(usuarioId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Error al obtener perfil"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(user: User): Result<Unit> {
        return try {
            val request = UpdateUserRequestDto(
                usuarioId = user.usuarioId,
                nombre = user.nombre,
                apellido = user.apellido,
                correo = user.correo,
                telefono = user.telefono,
            )
            Log.d("UserRepositoryImpl", "Debug backend: $request")
            val response = apiService.saveOrUpdateUser(request)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                if (dto.codigo == "200" || dto.codigo == "201" || dto.codigo == "success" || dto.mensaje?.contains("correctamente", ignoreCase = true) == true) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(dto.mensaje ?: "Error al actualizar perfil"))
                }
            } else {
                Result.failure(Exception("Error al actualizar perfil"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
