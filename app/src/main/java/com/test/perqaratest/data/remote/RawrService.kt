package com.test.perqaratest.data.remote

import com.test.perqaratest.data.models.Game
import com.test.perqaratest.data.models.GameList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RawrService {

    @GET("/api/games")
    suspend fun getGameList(
        @Query("page") page: Int,
        @Query("search") search: String?,
        @Query("key") key: String?
    ): Response<GameList>

    @GET("/api/games/{id}")
    suspend fun getGame(
        @Path("id") id: Int,
        @Query("key") key: String?
    ): Response<Game>

    companion object {
        const val BASE_URL = "https://api.rawg.io"
        const val key = "6798ce9cb97048b1a2567e9f43b6870f"
    }
}