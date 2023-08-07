package by.zharikov.photosonmap.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.zharikov.photosonmap.utils.Constants
import com.google.gson.annotations.SerializedName


@Entity(tableName = Constants.PHOTO_TABLE)
data class PhotoEntity(
    val date: Long,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "photo_id")
    val id: Int,
    val lat: Double,
    val lng: Double,
    val url: String
) {
    companion object {
        fun toPhotoEntity(data: PhotoDto.Data) =
            PhotoEntity(
                date = data.date,
                id = data.id,
                lat = data.lat,
                lng = data.lng,
                url = data.url
            )
    }
}