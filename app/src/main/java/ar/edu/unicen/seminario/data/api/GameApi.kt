package ar.edu.unicen.seminario.data.api

import ar.edu.unicen.seminario.data.model.Page
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GameApi {

    @GET("games")
    suspend fun getAllGames(
        @Query("key") apikey: String,
        @Query("page_size") pageSize: Int,
        @Query("platforms") platforms: String? = null,  // IDs separados por coma
        @Query("genres") genres: String? = null,        // IDs separados por coma
        @Query("dates") dates: String? = null,          // Formato: YYYY-01-01,YYYY-12-31
        @Query("metacritic") metacritic: String? = null, // Formato: 80,100
        @Query("ordering") ordering: String? = null      // Ej: "-released", "name"
    ): Response<Page>
}