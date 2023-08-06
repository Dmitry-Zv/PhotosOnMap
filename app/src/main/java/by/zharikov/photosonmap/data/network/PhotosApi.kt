package by.zharikov.photosonmap.data.network

import by.zharikov.photosonmap.domain.model.PhotoDto
import by.zharikov.photosonmap.domain.model.PostAuthentication
import by.zharikov.photosonmap.domain.model.PostPhoto
import by.zharikov.photosonmap.domain.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST

interface PhotosApi {


    @POST("/api/account/signup")
    suspend fun signUp(
        @Body postAuthentication: PostAuthentication
    ):Response<User>


    @POST("/api/account/signin")
    suspend fun signIn(
        @Body postAuthentication: PostAuthentication
    ):Response<User>


    @POST("/api/image")
    suspend fun uploadPhoto(
        @Body postPhoto: PostPhoto,
        @HeaderMap headers: Map<String,String>
    ):Response<PhotoDto>

    @GET("/api/image")
    suspend fun getPhotos(
        @HeaderMap headers: Map<String, String>
    ):Response<List<PhotoDto>>
}