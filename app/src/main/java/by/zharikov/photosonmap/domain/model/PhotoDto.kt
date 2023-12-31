package by.zharikov.photosonmap.domain.model

import com.google.gson.annotations.SerializedName

data class PhotoDto(
    @SerializedName("data")
    val data: Photo = Photo(),
    @SerializedName("status")
    val status: Int = 0
)