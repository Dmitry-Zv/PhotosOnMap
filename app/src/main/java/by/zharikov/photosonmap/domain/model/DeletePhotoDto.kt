package by.zharikov.photosonmap.domain.model


import com.google.gson.annotations.SerializedName

data class DeletePhotoDto(
    @SerializedName("data")
    val data: String?,
    @SerializedName("status")
    val status: Int = 0
)