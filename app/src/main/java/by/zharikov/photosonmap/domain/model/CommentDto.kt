package by.zharikov.photosonmap.domain.model


import com.google.gson.annotations.SerializedName

data class CommentDto(
    @SerializedName("data")
    val comment: Comment = Comment(),
    @SerializedName("status")
    val status: Int = 0
)