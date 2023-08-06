package by.zharikov.photosonmap.domain.model


import com.google.gson.annotations.SerializedName

data class PostPhoto(
    @SerializedName("base64Image")
    val base64Image: String = "",
    @SerializedName("date")
    val date: Int = 0,
    @SerializedName("lat")
    val lat: Int = 0,
    @SerializedName("lng")
    val lng: Int = 0
)