package by.zharikov.photosonmap.domain.model


import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("date")
    val date: Long = 0,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("text")
    val text: String = ""
)