package by.zharikov.photosonmap.di

import by.zharikov.photosonmap.data.repository.AuthenticationRepositoryImpl
import by.zharikov.photosonmap.data.repository.CommentRepositoryImpl
import by.zharikov.photosonmap.data.repository.PhotosRepositoryImpl
import by.zharikov.photosonmap.domain.repository.AuthenticationRepository
import by.zharikov.photosonmap.domain.repository.CommentRepository
import by.zharikov.photosonmap.domain.repository.PhotosRepository
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
}