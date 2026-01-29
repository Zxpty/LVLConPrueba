package com.example.lvlconprueba.domain.repositories

import com.example.lvlconprueba.domain.model.User

interface UserRepository {
    suspend fun getUserProfile(usuarioId: String): Result<User>
    suspend fun updateProfile(user: User): Result<Unit>
}
