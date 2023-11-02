package com.arfsar.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arfsar.storyapp.data.entities.ListStoryEntity
import com.arfsar.storyapp.databinding.CardStoryBinding
import com.arfsar.storyapp.ui.utils.loadImage

class StoryAdapter : PagingDataAdapter<ListStoryEntity, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class MyViewHolder(private val binding: CardStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stories: ListStoryEntity) {
            with(binding) {
                tvTitle.text = stories.name
                tvDescription.text = stories.description
                ivStory.loadImage(stories.photoUrl)
            }
        }
    }

    fun setOnItemClickCallback(onItemCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryEntity)
    }

    override fun onCreateViewHolder(view: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardStoryBinding.inflate(LayoutInflater.from(view.context), view, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listStories = getItem(position)
        if (listStories != null) {
            holder.bind(listStories)
            holder.itemView.setOnClickListener { getItem(position)?.let {
                onItemClickCallback.onItemClicked(
                    it
                )
            } }
        }
  }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryEntity>() {
            override fun areItemsTheSame(oldItem: ListStoryEntity, newItem: ListStoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryEntity,
                newItem: ListStoryEntity
            ): Boolean {
                return newItem == oldItem
            }
        }
    }
}