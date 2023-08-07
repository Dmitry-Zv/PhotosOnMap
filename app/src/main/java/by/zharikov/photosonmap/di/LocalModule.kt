package by.zharikov.photosonmap.di

import android.content.Context
import androidx.room.Room
import by.zharikov.photosonmap.data.local.PhotosDatabase
import by.zharikov.photosonmap.data.local.dao.PhotoDao
import by.zharikov.photosonmap.utils.Constants.PHOTO_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun providePhotosDatabase(@ApplicationContext context: Context): PhotosDatabase =
        Room.databaseBuilder(
            context,
            PhotosDatabase::class.java,
            PHOTO_DATABASE
        )
            .build()


    @Provides
    @Singleton
    fun providePhotoDao(database: PhotosDatabase): PhotoDao =
        database.getPhotoDao()
}