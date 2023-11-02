package com.arfsar.storyapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "story")
data class ListStoryEntity(

    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("lat")
    val lat: Double,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("lon")
    val lon: Double,
)


