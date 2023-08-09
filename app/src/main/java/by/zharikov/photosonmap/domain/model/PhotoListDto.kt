package by.zharikov.photosonmap.domain.model


import com.google.gson.annotations.SerializedName

data class PhotoListDto(
    @SerializedName("data")
    val data: List<Photo> = listOf(),
    @SerializedName("status")
    val status: Int = 0
)