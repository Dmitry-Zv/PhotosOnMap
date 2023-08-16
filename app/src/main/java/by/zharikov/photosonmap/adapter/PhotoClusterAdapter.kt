package by.zharikov.photosonmap.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.zharikov.photosonmap.R
import by.zharikov.photosonmap.databinding.ItemPhotoBinding
import by.zharikov.photosonmap.domain.model.PhotoUi
import by.zharikov.photosonmap.presentation.photos.PhotoClickListener
import com.bumptech.glide.Glide

class PhotoClusterAdapter(
    private val photoClickListener: PhotoClickListener
) :
    RecyclerView.Adapter<PhotoClusterAdapter.PhotoClusterViewHolder>() {

    inner class PhotoClusterViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: PhotoUi) {
            with(binding) {
                root.setOnClickListener {
                    photoClickListener.onPhotoClickListener(photo = photo)
                }
                Glide.with(root).load(photo.url)
                    .error(R.drawable.ic_no_image)
                    .into(photoView)
                dataView.text = photo.date
            }

        }
    }

    private val callback = object : DiffUtil.ItemCallback<PhotoUi>() {
        override fun areItemsTheSame(oldItem: PhotoUi, newItem: PhotoUi): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhotoUi, newItem: PhotoUi): Boolean {
            return oldItem == newItem
        }

    }


    private val listDiffer = AsyncListDiffer(this, callback)

    override fun onBindViewHolder(holder: PhotoClusterViewHolder, position: Int) {
        val photo = listDiffer.currentList[position]
        holder.bind(photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoClusterViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoClusterViewHolder(binding)
    }

    override fun getItemCount(): Int =
        listDiffer.currentList.size

    fun setData(listOfPhotoUi: List<PhotoUi>) = listDiffer.submitList(listOfPhotoUi)
}

