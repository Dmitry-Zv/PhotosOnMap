package by.zharikov.photosonmap.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import by.zharikov.photosonmap.data.local.dao.PhotoDao
import by.zharikov.photosonmap.data.local.dao.PhotoRemoteKeysDao
import by.zharikov.photosonmap.domain.model.PhotoDto
import by.zharikov.photosonmap.domain.model.PhotoEntity
import by.zharikov.photosonmap.domain.model.PhotoRemoteKeys

@Database(entities = [PhotoEntity::class, PhotoRemoteKeys::class], version = 1)
abstract class PhotosDatabase:RoomDatabase() {

    abstract fun getPhotoDao():PhotoDao
    abstract fun getPhotoRemoteKeysDao():PhotoRemoteKeysDao
}