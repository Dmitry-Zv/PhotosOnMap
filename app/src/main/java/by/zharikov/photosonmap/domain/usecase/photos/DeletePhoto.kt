package by.zharikov.photosonmap.domain.usecase.photos

import by.zharikov.photosonmap.domain.model.DeletePhotoDto
import by.zharikov.photosonmap.domain.repository.PhotosRepository
import by.zharikov.photosonmap.utils.Resource
import javax.inject.Inject

class DeletePhoto @Inject constructor(
    private val repository: PhotosRepository
) {

    suspend operator fun invoke(token: String, photoId: Int): Resource<DeletePhotoDto> =
        repository.deletePhoto(token = token, photoId = photoId)
}