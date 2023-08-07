package by.zharikov.photosonmap.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.zharikov.photosonmap.databinding.ItemPhotoBinding
import by.zharikov.photosonmap.domain.model.PhotoDto
import by.zharikov.photosonmap.domain.model.PhotoUi
import by.zharikov.photosonmap.presentation.photos.DeletePhotoListener
import com.bumptech.glide.Glide

class PhotoAdapter(private val deletePhotoListener: DeletePhotoListener) :
    PagingDataAdapter<PhotoUi, PhotoAdapter.PhotoViewHolder>(PhotosDiffCallback()) {

    inner class PhotoViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: PhotoUi) {
            with(binding) {
                root.setOnLongClickListener {
                    deletePhotoListener.onDeletePhotoLongClickListener(photoId = photo.id)
                    true
                }
                Glide.with(root).load(photo.url).into(photoView)
                dataView.text = photo.date
            }

        }
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = getItem(position) ?: return
        holder.bind(photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }
}


class PhotosDiffCallback : DiffUtil.ItemCallback<PhotoUi>() {

    override fun areItemsTheSame(oldItem: PhotoUi, newItem: PhotoUi): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PhotoUi, newItem: PhotoUi): Boolean {
        return oldItem == newItem
    }

}