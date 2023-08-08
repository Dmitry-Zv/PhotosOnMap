package by.zharikov.photosonmap.domain.model


import android.os.Parcelable
import by.zharikov.photosonmap.utils.formatToData
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoUi(
    val date: String,
    val id: Int,
    val lat: Double,
    val lng: Double,
    val url: String = ""
) : Parcelable {
    companion object {
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
