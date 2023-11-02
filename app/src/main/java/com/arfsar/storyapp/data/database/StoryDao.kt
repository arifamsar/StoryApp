package com.arfsar.storyapp.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arfsar.storyapp.data.entities.ListStoryEntity

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ListStoryEntity>)

    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, ListStoryEntity>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}