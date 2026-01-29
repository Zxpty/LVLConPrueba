package com.example.lvlconprueba.domain.usecase

import com.example.lvlconprueba.domain.model.User
import com.example.lvlconprueba.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(user: User): Flow<Result<Unit>> = flow {
        emit(repository.updateProfile(user))
    }
}
