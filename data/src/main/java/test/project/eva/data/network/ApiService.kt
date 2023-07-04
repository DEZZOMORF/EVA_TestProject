package test.project.eva.data.network

import retrofit2.Response
import retrofit2.http.GET
import test.project.eva.data.models.PhotoEntity

interface ApiService {

    @GET(NetworkUrls.GET_RANDOM_PHOTO)
    suspend fun getRandomPhoto(): Response<PhotoEntity>
}