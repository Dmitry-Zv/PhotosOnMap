package by.zharikov.photosonmap.domain.model


import com.google.gson.annotations.SerializedName

data class CommentListDto(
    @SerializedName("data")
    val commentList: List<Comment> = listOf(),
    @SerializedName("status")
    val status: Int = 0
)