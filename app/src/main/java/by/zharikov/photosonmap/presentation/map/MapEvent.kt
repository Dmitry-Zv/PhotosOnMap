package by.zharikov.photosonmap.presentation.map

sealed class MapEvent {

    object GetAllPhotos : MapEvent()
    data class GetPhotoById(
        val id: Int
    ) : MapEvent()

    data class ShowError(val exception: Exception) : MapEvent()

    data class PerformCurrentLocation(val latitude: Double, val longitude: Double) : MapEvent()

    object PerformMinskLocation : MapEvent()
}
