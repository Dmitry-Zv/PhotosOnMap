package by.zharikov.photosonmap.domain.usecase.map

import by.zharikov.photosonmap.domain.model.PhotoUi
import by.zharikov.photosonmap.domain.repository.MapRepository
import by.zharikov.photosonmap.utils.Resource
import javax.inject.Inject

class GetAllPhotos @Inject constructor(
    private val repository: MapRepository
) {

    suspend operator fun invoke(): Resource<List<PhotoUi>> =
        repository.getAllPhotos()
}