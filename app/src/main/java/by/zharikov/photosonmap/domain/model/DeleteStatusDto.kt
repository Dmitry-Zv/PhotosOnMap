package by.zharikov.photosonmap.domain.model


import com.google.gson.annotations.SerializedName

data class DeleteStatusDto(
    @SerializedName("data")
    val data: String?,
    @SerializedName("status")
    val status: Int = 0
)