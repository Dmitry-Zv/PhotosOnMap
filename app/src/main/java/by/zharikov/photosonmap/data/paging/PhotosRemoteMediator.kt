package by.zharikov.photosonmap.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import by.zharikov.photosonmap.data.local.PhotosDatabase
import by.zharikov.photosonmap.data.network.PhotosApi
import by.zharikov.photosonmap.domain.model.PhotoDto
import by.zharikov.photosonmap.domain.model.PhotoEntity
import by.zharikov.photosonmap.domain.model.PhotoRemoteKeys
import by.zharikov.photosonmap.utils.Constants.ACCESS_TOKEN
import okio.IOException
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class PhotosRemoteMediator(
    private val photosApi: PhotosApi,
    private val photosDatabase: PhotosDatabase,
    private val token: String
) : RemoteMediator<Int, PhotoEntity>() {

    private val photoRemoteKeysDao = photosDatabase.getPhotoRemoteKeysDao()
    private val photoDao = photosDatabase.getPhotoDao()

    private var pageIndex = 0


//    override suspend fun initialize(): InitializeAction {
//        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
//
//        return if (System.currentTimeMillis() - photoRemoteKeysDao.getCreationTime() < cacheTimeout) {
//
//            InitializeAction.SKIP_INITIAL_REFRESH
//        } else {
//            InitializeAction.LAUNCH_INITIAL_REFRESH
//        }
//    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoEntity>
    ): MediatorResult {

        pageIndex = getPageIndex(loadType) ?:
                return MediatorResult.Success(endOfPaginationReached = true)
        return  try {


            val response = photosApi.getPhotos(
                headers = mapOf(
                    ACCESS_TOKEN to token
                ),
                page = pageIndex
            )
            if (response.isSuccessful) {
                val photos = checkNotNull(response.body()).data
                val endOfPaginationReached = photos.isEmpty()
                val photosEntities = photos.map {
                    PhotoEntity.toPhotoEntity(it)
                }
                photosDatabase.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        photoDao.deleteAllPhotos()
//                        photoRemoteKeysDao.deleteAllRemoteKeys()
                    }
                    val prevPage = if (pageIndex == 0) null else pageIndex - 1
                    val nextPage = if (endOfPaginationReached) null else pageIndex + 1
//                    val keys = photos.map {
//                        PhotoRemoteKeys(
//                            id = it.id,
//                            prevPage = prevPage,
//                            nextPage = nextPage
//                        )
//                    }
//                    photoRemoteKeysDao.addAllRemoteKeys(keys)
                    photoDao.addPhotos(photosEntities)
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