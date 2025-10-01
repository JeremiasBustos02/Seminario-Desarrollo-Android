package ar.edu.unicen.seminario.data.model

data class GameFilters(
    val platform: String? = null,
    val genre: String? = null,
    val year: Int? = null,
    val minRating: Float? = null
)
