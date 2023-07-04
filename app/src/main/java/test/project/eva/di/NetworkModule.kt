package test.project.eva.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import test.project.eva.data.network.ApiService
import test.project.eva.data.network.NetworkUrls
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor {
                return@addInterceptor it.proceed(
                    it.request()
                        .newBuilder()
                        .addHeader("Authorization", "Client-ID " + "FVP5p0l3pEBp9vD4bChw1kbJOQaabZ9zgTC-d0kT-8A")
                        .build()
                )
            }
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(NetworkUrls.MAIN_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideRetrofitService(retrofit: Retrofit.Builder): ApiService {
        return retrofit
            .build()
            .create(ApiService::class.java)
    }
}