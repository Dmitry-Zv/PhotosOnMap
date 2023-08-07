package by.zharikov.photosonmap.data.network

import by.zharikov.photosonmap.domain.model.*
import retrofit2.Response
import retrofit2.http.*

interface PhotosApi {


    @POST("/api/account/signup")
    suspend fun signUp(
        @Body postAuthentication: PostAuthentication
    ): Response<User>


    @POST("/api/account/signin")
    suspend fun signIn(
        @Body postAuthentication: PostAuthentication
    ): Response<User>


    @POST("/api/image")
    suspend fun uploadPhoto(
        @Body postPhoto: PostPhoto,
        @HeaderMap headers: Map<String, String>
    ): Response<PhotoDto>

    @GET("/api/image")
    suspend fun getPhotos(
        @HeaderMap headers: Map<String, String>,
        @Query("page") page: Int
    ): Response<PhotoDto>


    @DELETE("/api/image/{id}")
    suspend fun deletePhoto(
        @Path("id") id:Int,
        @HeaderMap headers: Map<String, String>
    ):Response<DeletePhotoDto>
}