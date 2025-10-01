package ar.edu.unicen.seminario.data.model

class Page(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Game>
) {

    fun toGamesList(): List<Game> {
        return results
    }
}