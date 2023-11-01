package com.arfsar.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arfsar.storyapp.data.response.ListStoryItem
import com.arfsar.storyapp.databinding.CardStoryBinding
import com.arfsar.storyapp.ui.utils.loadImage

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class MyViewHolder(private val binding: CardStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stories: ListStoryItem) {
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
        fun onItemClicked(data: ListStoryItem)
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
//        holder.bind(listStories)
//        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(getItem(position)) }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return newItem == oldItem
            }
        }
    }
}