package by.zharikov.photosonmap.di

import by.zharikov.photosonmap.domain.repository.AuthenticationRepository
import by.zharikov.photosonmap.domain.repository.CommentRepository
import by.zharikov.photosonmap.domain.repository.MapRepository
import by.zharikov.photosonmap.domain.repository.PhotosRepository
import by.zharikov.photosonmap.domain.usecase.authentication.AuthenticationUseCases
import by.zharikov.photosonmap.domain.usecase.authentication.SignIn
import by.zharikov.photosonmap.domain.usecase.authentication.SignUp
import by.zharikov.photosonmap.domain.usecase.comment.CommentUseCases
import by.zharikov.photosonmap.domain.usecase.comment.DeleteComments
import by.zharikov.photosonmap.domain.usecase.comment.GetComments
import by.zharikov.photosonmap.domain.usecase.comment.UploadComment
import by.zharikov.photosonmap.domain.usecase.map.GetAllPhotos
import by.zharikov.photosonmap.domain.usecase.map.GetPhotoById
import by.zharikov.photosonmap.domain.usecase.map.MapUseCases
import by.zharikov.photosonmap.domain.usecase.photos.DeletePhoto
import by.zharikov.photosonmap.domain.usecase.photos.GetPhotos
import by.zharikov.photosonmap.domain.usecase.photos.PhotosUseCases
import by.zharikov.photosonmap.domain.usecase.photos.UploadPhoto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

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
            deletePhoto = DeletePhoto(repository),
            uploadPhoto = UploadPhoto(repository)
        )

    @Provides
    @Singleton
    fun provideCommentUseCases(repository: CommentRepository): CommentUseCases =
        CommentUseCases(
            uploadComment = UploadComment(repository),
            getComments = GetComments(repository),
            deleteComments = DeleteComments(repository)
        )

    @Provides
    @Singleton
    fun provideMapUseCases(repository: MapRepository): MapUseCases =
        MapUseCases(
            getAllPhotos = GetAllPhotos(repository),
            getPhotoById = GetPhotoById(repository)
        )


}