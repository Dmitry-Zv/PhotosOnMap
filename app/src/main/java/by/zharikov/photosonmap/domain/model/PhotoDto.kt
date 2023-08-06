package by.zharikov.photosonmap.domain.model


import com.google.gson.annotations.SerializedName

data class PhotoDto(
    @SerializedName("data")
    val data: Data = Data(),
    @SerializedName("status")
    val status: Int = 0
) {
    data class Data(
        @SerializedName("date")
        val date: Int = 0,
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("lat")
        val lat: Double = 0.0,
        @SerializedName("lng")
        val lng: Double = 0.0,
        @SerializedName("url")
        val url: String = ""
    )
}