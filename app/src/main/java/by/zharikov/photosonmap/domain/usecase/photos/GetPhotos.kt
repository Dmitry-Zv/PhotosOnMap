package by.zharikov.photosonmap.domain.usecase.photos

import androidx.paging.PagingData
import by.zharikov.photosonmap.domain.model.PhotoUi
import by.zharikov.photosonmap.domain.repository.PhotosRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotos @Inject constructor(
    private val repository: PhotosRepository
) {
    operator fun invoke(token: String): Flow<PagingData<PhotoUi>> =
        repository.getPagedPhotos(token = token)
}