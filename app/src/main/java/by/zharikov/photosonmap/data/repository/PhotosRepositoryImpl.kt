package by.zharikov.photosonmap.data.repository

import androidx.paging.*
import by.zharikov.photosonmap.data.local.PhotosDatabase
import by.zharikov.photosonmap.data.local.dao.CommentDao
import by.zharikov.photosonmap.data.local.dao.PhotoDao
import by.zharikov.photosonmap.data.network.PhotosApi
import by.zharikov.photosonmap.data.paging.PhotosRemoteMediator
import by.zharikov.photosonmap.domain.model.*
import by.zharikov.photosonmap.domain.repository.PhotosRepository
import by.zharikov.photosonmap.utils.Constants.ACCESS_TOKEN
import by.zharikov.photosonmap.utils.Constants.PAGE_SIZE
import by.zharikov.photosonmap.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(
    private val photosApi: PhotosApi,
    private val database: PhotosDatabase,
    private val photoDao: PhotoDao,
    private val commentDao: CommentDao
) : PhotosRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedPhotos(token: String): Flow<PagingData<PhotoUi>> {

        val pagingSourceFactory = { photoDao.getAllPhotos() }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE * 3
            ),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = PhotosRemoteMediator(
                photosApi, database, token
            )
        ).flow
            .map { pagingData ->
                pagingData.map {
                    PhotoUi.toPhotoUi(it)
                }

            }
    }

    override suspend fun deletePhoto(token: String, photoId: Int): Resource<DeleteStatusDto> {
        return try {
            val comments = commentDao.getAllCommentsByPhotoId(photoId = photoId)
            val listOfResponse = comments.map {
                photosApi.deleteComment(
                    imageId = photoId,
                    headers = mapOf(ACCESS_TOKEN to token),
                    commentId = it.id
                )
            }
            val allSuccessful = listOfResponse.all {
                it.isSuccessful
            }
            if (allSuccessful) {
                commentDao.deleteAllComments(photoId = photoId)
                val response =
                    photosApi.deletePhoto(id = photoId, headers = mapOf(ACCESS_TOKEN to token))
                if (response.isSuccessful) {
                    val deletePhotoDto = checkNotNull(response.body())
                    photoDao.deletePhotoById(photoId = photoId)
                    Resource.Success(data = deletePhotoDto)
                } else {
                    Resource.Error(HttpException(response))
                }
            } else {
                throw Exception("Trouble deleting comments")
            }

        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun uploadPhoto(token: String, postPhoto: PostPhoto): Resource<PhotoDto> {
        return try {
            val response =
                photosApi.uploadPhoto(headers = mapOf(ACCESS_TOKEN to token), postPhoto = postPhoto)

            if (response.isSuccessful) {
                val photoDto = checkNotNull(response.body())
                val photoEntity = PhotoEntity.toPhotoEntity(data = photoDto.data)
                photoDao.addPhoto(photoEntity = photoEntity)
                Resource.Success(photoDto)

            } else {
                Resource.Error(HttpException(response))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}