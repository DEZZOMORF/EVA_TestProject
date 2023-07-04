package test.project.eva.domain.usecase

import test.project.eva.domain.models.Photo
import test.project.eva.domain.repository.NetworkRepository
import test.project.eva.domain.states.RequestDataState

class GetRandomPhotoUseCase(private val repository: NetworkRepository) {
    suspend fun execute(): RequestDataState<Photo> {
        return repository.getRandomPhotoUseCase()
    }
}