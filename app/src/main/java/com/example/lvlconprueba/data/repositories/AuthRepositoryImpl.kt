package com.example.lvlconprueba.data.repositories

import com.example.lvlconprueba.data.api.AuthApiService
import com.example.lvlconprueba.data.dto.LoginRequestDto
import com.example.lvlconprueba.data.local.AuthManager
import com.example.lvlconprueba.data.mapper.toDomain
import com.example.lvlconprueba.domain.model.User
import com.example.lvlconprueba.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService,
    private val authManager: AuthManager
) : AuthRepository {

    override suspend fun login(usuario: String, password: String): Result<User> {
        return try {
            val response = apiService.login(LoginRequestDto(usuario, password))
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                if (dto.exito == true) {
                    Result.success(dto.toDomain())
                } else {
                    Result.failure(Exception(dto.mensaje ?: "Login failed"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun saveSession(user: User) {
        authManager.saveSession(user)
    }

    override suspend fun isLoggedIn(): Boolean {
        return authManager.isLoggedIn.first()
    }

    override suspend fun logout() {
        authManager.clearSession()
    }

    override suspend fun getCurrentUser(): User? {
        return authManager.getCurrentUser()
    }
}
