package by.zharikov.photosonmap.data.network

import by.zharikov.photosonmap.domain.model.*
import by.zharikov.photosonmap.domain.model.PostAuthentication
import by.zharikov.photosonmap.domain.model.PostPhoto
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
        @Path("id") id: Int,
        @HeaderMap headers: Map<String, String>
    ): Response<DeleteStatusDto>

    @POST("/api/image/{imageId}/comment")
    suspend fun uploadComment(
        @Body text: String,
        @HeaderMap headers: Map<String, String>,
        @Path("imageId") imageId: Int
    ): Response<CommentDto>

    @GET("/api/image/{imageId}/comment")
    suspend fun getComments(
        @HeaderMap headers: Map<String, String>,
        @Path("imageId") imageId: Int,
        @Query("page") page: Int
    ): Response<CommentListDto>

    @DELETE("/api/image/{imageId}/comment/{commentId}")
    suspend fun deleteComment(
        @HeaderMap headers: Map<String, String>,
        @Path("imageId") imageId: Int,
        @Path("commentId") commentId: Int
    ): Response<DeleteStatusDto>


}