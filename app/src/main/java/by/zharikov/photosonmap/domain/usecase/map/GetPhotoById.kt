package by.zharikov.photosonmap.domain.usecase.map

import by.zharikov.photosonmap.domain.model.PhotoUi
import by.zharikov.photosonmap.domain.repository.MapRepository
import by.zharikov.photosonmap.utils.Resource
import javax.inject.Inject

class GetPhotoById @Inject constructor(
    private val repository: MapRepository
) {
    suspend operator fun invoke(photoId: Int): Resource<PhotoUi> =
        repository.getPhotoById(photoId = photoId)
}