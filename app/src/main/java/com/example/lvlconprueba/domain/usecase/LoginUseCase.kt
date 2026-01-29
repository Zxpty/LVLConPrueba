package com.example.lvlconprueba.domain.usecase

import com.example.lvlconprueba.data.repositories.AuthRepositoryImpl
import com.example.lvlconprueba.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepositoryImpl
) {
    operator fun invoke(usuario: String, password: String): Flow<Result<User>> = flow {
        emit(repository.login(usuario, password))
    }

    suspend fun getCurrentUser(): User? {
        return repository.getCurrentUser()
    }


    suspend fun saveSession(user: User) {
        repository.saveSession(user)
    }

    suspend fun isLoggedIn(): Boolean {
        return repository.isLoggedIn()
    }
}
