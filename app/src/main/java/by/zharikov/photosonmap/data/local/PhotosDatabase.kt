package by.zharikov.photosonmap.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import by.zharikov.photosonmap.data.local.dao.CommentDao
import by.zharikov.photosonmap.data.local.dao.PhotoDao
import by.zharikov.photosonmap.domain.model.CommentEntity
import by.zharikov.photosonmap.domain.model.PhotoEntity

@Database(entities = [PhotoEntity::class, CommentEntity::class], version = 1)
abstract class PhotosDatabase : RoomDatabase() {

    abstract fun getPhotoDao(): PhotoDao

    abstract fun getCommentDao(): CommentDao
}