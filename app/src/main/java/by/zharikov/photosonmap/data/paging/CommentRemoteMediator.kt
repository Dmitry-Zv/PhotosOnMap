package by.zharikov.photosonmap.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import by.zharikov.photosonmap.data.local.PhotosDatabase
import by.zharikov.photosonmap.data.network.PhotosApi
import by.zharikov.photosonmap.domain.model.CommentEntity
import by.zharikov.photosonmap.utils.Constants
import okio.IOException
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class CommentRemoteMediator(
    private val photosApi: PhotosApi,
    private val database: PhotosDatabase,
    private val token:String,
    private val imageId: Int
):RemoteMediator<Int, CommentEntity>() {

    private val commentDao = database.getCommentDao()
    private var pageIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CommentEntity>
    ): MediatorResult {
        pageIndex = getPageIndex(loadType) ?:
                return MediatorResult.Success(endOfPaginationReached = true)
        return  try {


            val response = photosApi.getComments(
                headers = mapOf(
                    Constants.ACCESS_TOKEN to token
                ),
                page = pageIndex,
                imageId = imageId
            )
            if (response.isSuccessful) {
                val comments = checkNotNull(response.body()).commentList
                val endOfPaginationReached = comments.isEmpty()
                val commentEntities = comments.map {
                    CommentEntity.toCommentEntity(it, imageId)
                }
                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        commentDao.deleteAllComments(photoId = imageId)
                    }
                    commentDao.addComments(comments = commentEntities)
                }
                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } else {
                MediatorResult.Error(HttpException(response))
            }
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }


    private fun getPageIndex(loadType: LoadType): Int? {
        when (loadType) {
            LoadType.REFRESH -> pageIndex = 0
            LoadType.PREPEND -> return null
            LoadType.APPEND -> pageIndex++
        }
        return pageIndex
    }
}