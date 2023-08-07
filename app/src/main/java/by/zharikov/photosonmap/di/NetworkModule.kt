package by.zharikov.photosonmap.di

import by.zharikov.photosonmap.data.network.PhotosApi
import by.zharikov.photosonmap.domain.repository.AuthenticationRepository
import by.zharikov.photosonmap.domain.repository.PhotosRepository
import by.zharikov.photosonmap.domain.usecase.authentication.AuthenticationUseCases
import by.zharikov.photosonmap.domain.usecase.authentication.SignIn
import by.zharikov.photosonmap.domain.usecase.authentication.SignUp
import by.zharikov.photosonmap.domain.usecase.photos.DeletePhoto
import by.zharikov.photosonmap.domain.usecase.photos.GetPhotos
import by.zharikov.photosonmap.domain.usecase.photos.PhotosUseCases
import by.zharikov.photosonmap.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoginInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(loginInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loginInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Provides
    @Singleton
    fun providePhotosApi(retrofit: Retrofit): PhotosApi =
        retrofit.create(PhotosApi::class.java)

    @Provides
    @Singleton
    fun provideAuthenticationUseCases(repository: AuthenticationRepository): AuthenticationUseCases =
        AuthenticationUseCases(
            signIn = SignIn(repository),
            signUp = SignUp(repository)
        )

    @Provides
    @Singleton
    fun providePhotosUseCases(repository: PhotosRepository): PhotosUseCases =
        PhotosUseCases(
            getPhotos = GetPhotos(repository),
            deletePhoto = DeletePhoto(repository = repository)
        )
}