package com.test.perqaratest.domain.repository

import com.test.perqaratest.data.models.Game
import com.test.perqaratest.data.models.GameList
import com.test.perqaratest.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    fun getGameList(page: Int, search: String?): Flow<Result<GameList>>

    fun getGame(id: Int): Flow<Result<Game>>

    suspend fun addToBookmark(game: Game)

    suspend fun removeBookmark(id: Int)

    fun getBookmarkedGames(): Flow<List<Game>>
}