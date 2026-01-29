package com.example.lvlconprueba.domain.usecase

import com.example.lvlconprueba.domain.model.Project
import com.example.lvlconprueba.domain.repositories.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(project: Project): Flow<Result<Unit>> = flow {
        emit(repository.saveOrUpdateProject(project))
    }
}
