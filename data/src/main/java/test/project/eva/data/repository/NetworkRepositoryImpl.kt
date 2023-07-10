package test.project.eva.data.repository

import test.project.eva.data.mappers.PhotoMapper
import test.project.eva.data.network.ApiService
import test.project.eva.domain.models.Photo
import test.project.eva.domain.repository.NetworkRepository
import test.project.eva.domain.states.RequestDataState

class NetworkRepositoryImpl(private val apiService: ApiService): NetworkRepository {

    override suspend fun getRandomPhotoUseCase(): RequestDataState<Photo> {
        return try {
            val response = apiService.getRandomPhoto()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                val photo = PhotoMapper().mapFromEntity(body)
                RequestDataState.Success(photo)
            } else {
                throw Exception("Response exception: $response")
            }
        } catch (e: Exception) {
            RequestDataState.Error(e)
        }
    }
}