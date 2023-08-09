package by.zharikov.photosonmap.presentation.map

import by.zharikov.photosonmap.domain.model.PhotoUi
import com.google.android.gms.maps.model.LatLng

data class MapState(
    val msgError: String?,
    val listOfPhotoUi: List<PhotoUi>,
    val photoUi: PhotoUi?,
    val location:LatLng?
) {
    companion object {
        val default = MapState(
            msgError = null,
            listOfPhotoUi = emptyList(),
            photoUi = null,
            location = null
        )
    }
}