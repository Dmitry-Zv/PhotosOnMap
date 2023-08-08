package by.zharikov.photosonmap.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class PhotoWithComments(
    @Embedded
    val photoEntity: PhotoEntity,
    @Relation(
        parentColumn = "photo_id",
        entityColumn = "photo_id"
    )
    val comments:List<CommentEntity>

)
