package by.zharikov.photosonmap.domain.model


import com.google.gson.annotations.SerializedName

data class PostPhoto(
    @SerializedName("base64Image")
    val base64Image: String = "",
    @SerializedName("date")
    val date: Long = 0,
    @SerializedName("lat")
    val lat: Double = 0.0,
    @SerializedName("lng")
    val lng: Double = 0.0
)