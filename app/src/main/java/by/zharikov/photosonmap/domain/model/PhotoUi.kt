package by.zharikov.photosonmap.domain.model


import by.zharikov.photosonmap.utils.formatToData
import com.google.gson.annotations.SerializedName
import java.util.Date

data class PhotoUi(
    val date: String,
    val id: Int,
    val lat: Double,
    val lng: Double,
    val url: String = ""
){
    companion object{
        fun toPhotoUi(photoEntity: PhotoEntity) =
            PhotoUi(
                date = photoEntity.date.formatToData(photoEntity.date),
                id = photoEntity.id,
                lat = photoEntity.lat,
                lng = photoEntity.lng,
                url = photoEntity.url
            )
    }
}
