package by.zharikov.photosonmap.data.local.dao

import android.provider.ContactsContract.Contacts.Photo
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.zharikov.photosonmap.domain.model.PhotoDto
import by.zharikov.photosonmap.domain.model.PhotoEntity

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPhotos(photos: List<PhotoEntity>)

    @Query("SELECT * FROM photo_table")
    fun getAllPhotos(): PagingSource<Int, PhotoEntity>

    @Query("DELETE FROM photo_table")
    suspend fun deleteAllPhotos()

    @Query("DELETE FROM photo_table WHERE photo_id = :photoId")
    suspend fun deletePhotoById(photoId:Int)


}