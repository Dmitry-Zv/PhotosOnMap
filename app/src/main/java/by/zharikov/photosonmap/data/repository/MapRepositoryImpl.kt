package by.zharikov.photosonmap.data.repository

import by.zharikov.photosonmap.data.local.dao.PhotoDao
import by.zharikov.photosonmap.domain.model.PhotoUi
import by.zharikov.photosonmap.domain.repository.MapRepository
import by.zharikov.photosonmap.utils.Resource
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val photoDao: PhotoDao
) : MapRepository {

    override suspend fun getAllPhotos(): Resource<List<PhotoUi>> {
        return try {
            val photoEntities = photoDao.getAllPhotosInDb()
            val listOfPhotoUi = photoEntities.map {
                PhotoUi.toPhotoUi(photoEntity = it)
            }

            Resource.Success(data = listOfPhotoUi)
        } catch (e: Exception) {
            Resource.Error(e)

        }
    }

    override suspend fun getPhotoById(photoId: Int): Resource<PhotoUi> {
        return try {
            val photoEntity = photoDao.getPhotoById(photoId = photoId)
            val photoUi = PhotoUi.toPhotoUi(photoEntity = photoEntity)
            Resource.Success(data = photoUi)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}