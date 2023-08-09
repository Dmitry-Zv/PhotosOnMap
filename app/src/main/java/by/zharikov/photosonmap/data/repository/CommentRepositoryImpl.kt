package by.zharikov.photosonmap.data.repository

import androidx.paging.*
import by.zharikov.photosonmap.data.local.PhotosDatabase
import by.zharikov.photosonmap.data.local.dao.CommentDao
import by.zharikov.photosonmap.data.network.PhotosApi
import by.zharikov.photosonmap.data.paging.CommentRemoteMediator
import by.zharikov.photosonmap.domain.model.*
import by.zharikov.photosonmap.domain.repository.CommentRepository
import by.zharikov.photosonmap.utils.Constants
import by.zharikov.photosonmap.utils.Constants.ACCESS_TOKEN
import by.zharikov.photosonmap.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject


class CommentRepositoryImpl @Inject constructor(
    private val photosApi: PhotosApi,
    private val database: PhotosDatabase,
    private val commentDao: CommentDao
) :
    CommentRepository {

    override suspend fun uploadComment(
        text: String,
        token: String,
        imageId: Int
    ): Resource<CommentDto> {
        return try {
            val response = photosApi.uploadComment(
                text = text,
                headers = mapOf(ACCESS_TOKEN to token),
                imageId = imageId
            )
            if (response.isSuccessful) {
                val commentDto = checkNotNull(response.body())
                val commentEntity =
                    CommentEntity.toCommentEntity(data = commentDto.comment, photoId = imageId)
                commentDao.addComment(commentEntity)
                Resource.Success(data = commentDto)
            } else {
                Resource.Error(HttpException(response))
            }

        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedComment(token: String, imageId: Int): Flow<PagingData<CommentUi>> {
        val pagingSourceFactory = { commentDao.getAllComments(photoId = imageId) }
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                initialLoadSize = Constants.PAGE_SIZE * 3
            ),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = CommentRemoteMediator(
                photosApi, database, token, imageId
            )
        ).flow
            .map { pagingData ->
                pagingData.map {
                    CommentUi.toCommentUi(it)
                }

            }
    }


    override suspend fun deleteComment(
        token: String,
        imageId: Int,
        commentId: Int
    ): Resource<DeleteStatusDto> {
        return try {
            val response =
                photosApi.deleteComment(
                    headers = mapOf(ACCESS_TOKEN to token),
                    imageId = imageId,
                    commentId = commentId
                )
            if (response.isSuccessful) {
                val deleteStatusDto = checkNotNull(response.body())
                commentDao.deleteCommentById(commentId = commentId)
                Resource.Success(data = deleteStatusDto)
            } else {
                Resource.Error(HttpException(response))
            }

        } catch (e: Exception) {
            Resource.Error(e)
        }
    }


}