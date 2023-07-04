package test.project.eva.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import test.project.eva.data.network.ApiService
import test.project.eva.data.repository.GalleryRepositoryImpl
import test.project.eva.data.repository.NetworkRepositoryImpl
import test.project.eva.domain.usecase.GetRandomPhotoUseCase
import test.project.eva.domain.usecase.SavePhotoUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideSavePhotoUseCase(@ApplicationContext context: Context): SavePhotoUseCase {
        return SavePhotoUseCase(GalleryRepositoryImpl(context))
    }

    @Singleton
    @Provides
    fun provideGetRandomPhotoUseCase(@ApplicationContext context: Context, apiService: ApiService): GetRandomPhotoUseCase {
        return GetRandomPhotoUseCase(NetworkRepositoryImpl(context, apiService))
    }
}