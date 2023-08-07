package by.zharikov.photosonmap.domain.repository

import androidx.paging.PagingData
import by.zharikov.photosonmap.domain.model.DeletePhotoDto
import by.zharikov.photosonmap.domain.model.PhotoDto
import by.zharikov.photosonmap.domain.model.PhotoUi
import by.zharikov.photosonmap.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {


    fun getPagedPhotos(token:String): Flow<PagingData<PhotoUi>>


    suspend fun deletePhoto(token: String, photoId:Int):Resource<DeletePhotoDto>
}