package by.zharikov.photosonmap.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.zharikov.photosonmap.utils.Constants.PHOTO_REMOTE_KEYS_TABLE


@Entity(tableName = PHOTO_REMOTE_KEYS_TABLE)
data class PhotoRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val prevPage: Int?,
    val nextPage: Int?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)