package test.project.eva.data.repository

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import test.project.eva.data.mappers.PhotoMapper
import test.project.eva.data.network.ApiService
import test.project.eva.domain.models.Photo
import test.project.eva.domain.repository.NetworkRepository
import test.project.eva.domain.states.RequestDataState

class NetworkRepositoryImpl(private val context: Context, private val apiService: ApiService): NetworkRepository {

    override suspend fun getRandomPhotoUseCase(): RequestDataState<Photo> {
        return try {
            val response = apiService.getRandomPhoto()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                val drawable = loadDrawable(body.urls?.regular)
                val photo = PhotoMapper(drawable).mapFromEntity(body)
                RequestDataState.Success(photo)
            } else {
                throw Exception("Response exception: $response")
            }
        } catch (e: Exception) {
            RequestDataState.Error(e)
        }
    }

    private fun loadDrawable(url: String?): Drawable {
        return Glide
            .with(context)
            .asDrawable()
            .load(url)
            .submit()
            .get()
    }
}