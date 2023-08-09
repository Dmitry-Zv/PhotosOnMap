package by.zharikov.photosonmap.domain.model

import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("date")
    val date: Long = 0,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("lat")
    val lat: Double = 0.0,
    @SerializedName("lng")
    val lng: Double = 0.0,
    @SerializedName("url")
    val url: String = ""
)
