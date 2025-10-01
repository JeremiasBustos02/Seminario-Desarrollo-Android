package ar.edu.unicen.seminario.data.repository

import ar.edu.unicen.seminario.data.datasource.GameRemoteDataSource
import ar.edu.unicen.seminario.data.model.Game
import ar.edu.unicen.seminario.data.model.GameFilters
import javax.inject.Inject

class GameRepository @Inject constructor(
    private val remoteDataSource: GameRemoteDataSource
) {

    suspend fun getAllGames(
        pageSize: Int,
        filters: GameFilters? = null
    ): List<Game> {
        return remoteDataSource.getAllGames(pageSize, filters)
    }
}