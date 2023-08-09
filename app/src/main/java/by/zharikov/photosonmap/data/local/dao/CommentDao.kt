package by.zharikov.photosonmap.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.zharikov.photosonmap.domain.model.CommentEntity

@Dao
interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addComments(comments: List<CommentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addComment(comment: CommentEntity)

    @Query("SELECT * FROM comment_table WHERE photo_id = :photoId ORDER BY date_time DESC")
    fun getAllComments(photoId: Int): PagingSource<Int, CommentEntity>

    @Query("SELECT * FROM comment_table WHERE photo_id = :photoId")
    suspend fun getAllCommentsByPhotoId(photoId: Int): List<CommentEntity>

    @Query("DELETE FROM comment_table WHERE photo_id = :photoId")
    suspend fun deleteAllComments(photoId: Int)

    @Query("DELETE FROM comment_table WHERE comment_id = :commentId")
    suspend fun deleteCommentById(commentId: Int)


}