package by.zharikov.photosonmap.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.zharikov.photosonmap.utils.Constants.COMMENT_TABLE

@Entity(tableName = COMMENT_TABLE)
data class CommentEntity(
    @ColumnInfo("date_time")
    val date: Long,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("comment_id")
    val id: Int,
    @ColumnInfo("photo_id")
    val photoId: Int,
    val text: String
) {
    companion object {
        fun toCommentEntity(data: Comment, photoId: Int) =
            CommentEntity(
                date = data.date,
                id = data.id,
                text = data.text,
                photoId = photoId
            )

    }
}
