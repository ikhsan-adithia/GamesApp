package com.test.perqaratest.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Game(

	@field:SerializedName("added")
	val added: Int,

	@field:SerializedName("name_original")
	val nameOriginal: String,

	@field:SerializedName("rating")
	val rating: Double,

	@PrimaryKey
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("released")
	val released: String,

	@field:SerializedName("background_image")
	val backgroundImage: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	val isBookmarked: Boolean = false
)

data class PublishersItem(

	@field:SerializedName("name")
	val name: String,

)
