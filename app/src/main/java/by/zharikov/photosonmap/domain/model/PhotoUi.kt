package by.zharikov.photosonmap.domain.model


import android.os.Parcelable
import by.zharikov.photosonmap.utils.formatToData
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoUi(
    val date: String = "",
    val id: Int = 0,
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val url: String = ""
) : Parcelable, ClusterItem {
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

    override fun getPosition(): LatLng {
        return LatLng(lat, lng)
    }

    override fun getTitle(): String {
        return id.toString()
    }

    override fun getSnippet(): String {
        return ""
    }
}
