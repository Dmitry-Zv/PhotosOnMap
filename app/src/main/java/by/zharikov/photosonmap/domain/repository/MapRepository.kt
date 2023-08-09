package by.zharikov.photosonmap.domain.repository

import by.zharikov.photosonmap.domain.model.PhotoUi
import by.zharikov.photosonmap.utils.Resource

interface MapRepository {


    suspend fun getAllPhotos(): Resource<List<PhotoUi>>

    suspend fun getPhotoById(photoId: Int): Resource<PhotoUi>
}