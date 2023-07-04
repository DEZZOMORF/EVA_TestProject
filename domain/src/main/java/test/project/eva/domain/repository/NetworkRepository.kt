package test.project.eva.domain.repository

import test.project.eva.domain.models.Photo
import test.project.eva.domain.states.RequestDataState

interface NetworkRepository {
    suspend fun getRandomPhotoUseCase(): RequestDataState<Photo>
}