package com.example.lvlconprueba.domain.repositories

import com.example.lvlconprueba.domain.model.User
interface AuthRepository {
    suspend fun login(usuario: String, password: String): Result<User>
    suspend fun saveSession(user: User)
    suspend fun isLoggedIn(): Boolean
    suspend fun logout()
    suspend fun getCurrentUser(): User?
}
