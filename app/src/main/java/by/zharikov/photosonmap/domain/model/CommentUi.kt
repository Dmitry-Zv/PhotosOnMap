package by.zharikov.photosonmap.domain.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import by.zharikov.photosonmap.utils.formatToData

data class CommentUi(
    val date: String,
    val id: Int,
    val photoId:Int,
    val text: String
){
    companion object{
        fun toCommentUi(commentEntity: CommentEntity) =
            CommentUi(
                date = commentEntity.date.formatToData(commentEntity.date),
                id = commentEntity.id,
                photoId = commentEntity.photoId,
                text = commentEntity.text
            )
    }
}
