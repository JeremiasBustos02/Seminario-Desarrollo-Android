package ar.edu.unicen.seminario.data.model

import com.google.gson.annotations.SerializedName

class Game (
    val id: Int,
    val name: String,
    val released: String?,
    @SerializedName("background_image")
    val background_image: String?,
    val genre: String,
    val rating: Number,
    val platforms: List<PlatformWrapper>?
)