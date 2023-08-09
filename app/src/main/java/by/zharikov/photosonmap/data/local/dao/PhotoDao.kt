package by.zharikov.photosonmap.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.zharikov.photosonmap.domain.model.CommentEntity
import by.zharikov.photosonmap.domain.model.PhotoEntity

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPhotos(photos: List<PhotoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPhoto(photoEntity: PhotoEntity)


    @Query("SELECT * FROM photo_table ORDER BY date_time DESC")
    fun getAllPhotos(): PagingSource<Int, PhotoEntity>

    @Query("DELETE FROM photo_table")
    suspend fun deleteAllPhotos()

    @Query("DELETE FROM photo_table WHERE photo_id = :photoId")
    suspend fun deletePhotoById(photoId:Int)


}