package com.arfsar.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arfsar.storyapp.data.response.ListStoryItem
import com.arfsar.storyapp.databinding.CardStoryBinding
import com.bumptech.glide.Glide

class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class MyViewHolder(private val binding: CardStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stories: ListStoryItem) {
            binding.tvTitle.text = stories.name
            binding.tvDescription.text = stories.description
            Glide.with(itemView.context)
                .load(stories.photoUrl)
                .skipMemoryCache(true)
                .into(binding.ivStory)
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
        holder.bind(listStories)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(getItem(position)) }
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