package test.project.eva.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import test.project.eva.domain.repository.NetworkRepository
import test.project.eva.domain.usecase.GetRandomPhotoUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideGetRandomPhotoUseCase(networkRepository: NetworkRepository): GetRandomPhotoUseCase {
        return GetRandomPhotoUseCase(networkRepository)
    }
}