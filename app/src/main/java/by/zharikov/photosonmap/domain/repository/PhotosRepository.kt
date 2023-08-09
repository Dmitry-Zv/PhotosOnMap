package by.zharikov.photosonmap.domain.repository

import androidx.paging.PagingData
import by.zharikov.photosonmap.domain.model.*
import by.zharikov.photosonmap.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {


    fun getPagedPhotos(token: String): Flow<PagingData<PhotoUi>>


    suspend fun deletePhoto(token: String, photoId: Int): Resource<DeleteStatusDto>

    suspend fun uploadPhoto(token: String, postPhoto: PostPhoto): Resource<PhotoDto>
}