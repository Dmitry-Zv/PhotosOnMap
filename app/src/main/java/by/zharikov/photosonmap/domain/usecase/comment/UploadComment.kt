package by.zharikov.photosonmap.domain.usecase.comment

import by.zharikov.photosonmap.domain.model.CommentDto
import by.zharikov.photosonmap.domain.repository.CommentRepository
import by.zharikov.photosonmap.utils.Resource
import javax.inject.Inject

class UploadComment @Inject constructor(private val repository: CommentRepository) {

    suspend operator fun invoke(text: String, token: String, imageId: Int): Resource<CommentDto> {
       return try {

           if (text.isBlank()) throw Exception("Text is empty.")

           repository.uploadComment(text, token, imageId)
       }catch (e:Exception){
           Resource.Error(e)
       }

    }
}