package by.zharikov.photosonmap.domain.usecase.photos

import by.zharikov.photosonmap.domain.model.PhotoDto
import by.zharikov.photosonmap.domain.model.PostPhoto
import by.zharikov.photosonmap.domain.repository.PhotosRepository
import by.zharikov.photosonmap.utils.Resource
import javax.inject.Inject

class UploadPhoto @Inject constructor(
    private val repository: PhotosRepository
) {
    suspend operator fun invoke(
        token: String,
        base64Image: String,
        latitude: Double,
        longitude: Double,
        currentTime: Long
    ): Resource<PhotoDto> {

        return try {
            if (base64Image.isBlank()) throw Exception("No photo provided.")
            val postPhoto = PostPhoto(
                base64Image = base64Image,
                date = currentTime,
                lat = latitude,
                lng = longitude
            )
            return repository.uploadPhoto(token = token, postPhoto = postPhoto)
        } catch (e: Exception) {
            Resource.Error(e)
        }

    }
}