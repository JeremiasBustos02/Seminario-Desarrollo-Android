package ar.edu.unicen.seminario.data.datasource

import ar.edu.unicen.seminario.BuildConfig
import ar.edu.unicen.seminario.data.api.GameApi
import ar.edu.unicen.seminario.data.model.Game
import ar.edu.unicen.seminario.data.model.GameFilters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GameRemoteDataSource @Inject constructor(
    private val gamesApi: GameApi
) {

    suspend fun getAllGames(pageSize: Int, filters: GameFilters? = null): List<Game> {
        return withContext(Dispatchers.IO) {
            val response = gamesApi.getAllGames(
                apikey = BuildConfig.apiKey,
                pageSize = pageSize,
                platforms = filters?.platform?.let { mapPlatformToId(it) },
                genres = filters?.genre?.let { mapGenreToId(it) },
                dates = filters?.year?.let { "$it-01-01,$it-12-31" },
                metacritic = filters?.minRating?.let {
                    val ratingInt = (it * 10).toInt() // Convierte 0-10 a 0-100
                    "$ratingInt,100"
                }
            )
            val gamesList = response.body()?.toGamesList() ?: emptyList()
            return@withContext gamesList
        }
    }

    // Mapea nombres de plataformas a IDs de la API de RAWG
    private fun mapPlatformToId(platform: String): String? {
        return when (platform) {
            "PC" -> "4"
            "PlayStation 5" -> "187"
            "PlayStation 4" -> "18"
            "Xbox Series X/S" -> "186"
            "Xbox One" -> "1"
            "Nintendo Switch" -> "7"
            "iOS" -> "3"
            "Android" -> "21"
            else -> null
        }
    }

    // Mapea nombres de géneros a IDs de la API de RAWG
    private fun mapGenreToId(genre: String): String? {
        return when (genre) {
            "Action" -> "4"
            "Adventure" -> "3"
            "RPG" -> "5"
            "Strategy" -> "10"
            "Shooter" -> "2"
            "Puzzle" -> "7"
            "Sports" -> "15"
            "Racing" -> "1"
            else -> null
        }
    }
}