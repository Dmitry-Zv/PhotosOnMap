package by.zharikov.photosonmap.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.zharikov.photosonmap.domain.model.PhotoRemoteKeys


@Dao
interface PhotoRemoteKeysDao {

    @Query("SELECT * FROM photo_remote_keys_table WHERE id =:id")
    suspend fun getRemoteKeys(id: Int): PhotoRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<PhotoRemoteKeys>)

    @Query("DELETE FROM photo_remote_keys_table")
    suspend fun deleteAllRemoteKeys()

    @Query("Select created_at From photo_remote_keys_table Order By created_at DESC LIMIT 1")
    suspend fun getCreationTime(): Long
}