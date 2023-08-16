package by.zharikov.photosonmap.di

import by.zharikov.photosonmap.data.repository.*
import by.zharikov.photosonmap.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindAuthenticationRepositoryImpl_toAuthenticationRepository(authenticationRepositoryImpl: AuthenticationRepositoryImpl): AuthenticationRepository

    @Binds
    @Singleton
    fun bindPhotosRepositoryImpl_toPhotosRepository(photosRepositoryImpl: PhotosRepositoryImpl): PhotosRepository

    @Binds
    @Singleton
    fun bindCommentRepositoryImpl_toCommentRepository(commentRepositoryImpl: CommentRepositoryImpl): CommentRepository

    @Binds
    @Singleton
    fun bindMapRepositoryImpl_toMapRepository(mpRepositoryImpl: MapRepositoryImpl): MapRepository

    @Binds
    @Singleton
    fun bindSharedPreferencesUserRepositoryImpl_toSharedPreferencesUserRepository(
        sharedPreferencesUserRepositoryImpl: SharedPreferencesUserRepositoryImpl
    ): SharedPreferencesUserRepository
}