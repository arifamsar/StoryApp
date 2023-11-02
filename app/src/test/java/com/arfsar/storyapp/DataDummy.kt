package com.arfsar.storyapp

import com.arfsar.storyapp.data.entities.ListStoryEntity

object DataDummy {
    fun generateDummyStoriesResponse(): List<ListStoryEntity> {
        val items: MutableList<ListStoryEntity> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryEntity(
                i.toString(),
                "photoUrl + $i",
                "name + $i",
                "description + $i",
                i.toDouble(),
                "createAt + $i",
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}