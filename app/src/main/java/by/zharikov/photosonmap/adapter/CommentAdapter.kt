package by.zharikov.photosonmap.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.zharikov.photosonmap.databinding.ItemCommentBinding
import by.zharikov.photosonmap.domain.model.CommentUi

class CommentAdapter :
    PagingDataAdapter<CommentUi, CommentAdapter.CommentViewHolder>(CommentDiffCallback()) {


    inner class CommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(commentUi: CommentUi) {
            with(binding) {
                textView.text = commentUi.text
                dataView.text = commentUi.date
            }
        }
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val commentUi = getItem(position) ?: return
        holder.bind(commentUi = commentUi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

}


class CommentDiffCallback : DiffUtil.ItemCallback<CommentUi>() {

    override fun areItemsTheSame(oldItem: CommentUi, newItem: CommentUi): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CommentUi, newItem: CommentUi): Boolean {
        return oldItem == newItem
    }

}