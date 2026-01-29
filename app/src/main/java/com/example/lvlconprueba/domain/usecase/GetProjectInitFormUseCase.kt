package com.example.lvlconprueba.domain.usecase

import com.example.lvlconprueba.domain.model.ProjectComboData
import com.example.lvlconprueba.domain.repositories.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProjectInitFormUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(): Flow<Result<ProjectComboData>> = flow {
        emit(repository.getInitForm())
    }
}
