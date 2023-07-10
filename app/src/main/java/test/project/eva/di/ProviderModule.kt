package test.project.eva.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import test.project.eva.provider.gallery.GalleryProvider
import test.project.eva.provider.gallery.GalleryProviderImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProviderModule {

    @Singleton
    @Provides
    fun provideGalleryProvider(galleryProvider: GalleryProviderImpl): GalleryProvider = galleryProvider
}