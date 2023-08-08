package by.zharikov.photosonmap.presentation.photos

import by.zharikov.photosonmap.domain.model.PhotoUi

interface PhotoClickListener {

    fun onPhotoClickListener(photo: PhotoUi)
}