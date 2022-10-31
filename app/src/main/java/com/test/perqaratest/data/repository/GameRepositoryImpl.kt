package com.test.perqaratest.data.repository

import com.test.perqaratest.data.local.GameDao
import com.test.perqaratest.data.models.Game
import com.test.perqaratest.data.models.GameList
import com.test.perqaratest.data.remote.RawrService
import com.test.perqaratest.data.utils.Result
import com.test.perqaratest.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GameRepositoryImpl(
    private val service: RawrService,
    private val dao: GameDao
): GameRepository {

    override suspend fun addToBookmark(game: Game) {
        dao.addToBookmark(game.copy(isBookmarked = true))
    }

    override suspend fun removeBookmark(id: Int) {
        dao.removeBookmark(id)
    }

    override fun getBookmarkedGames(): Flow<List<Game>> =
        dao.getBookmarkedGames()

    override fun getGameList(page: Int, search: String?): Flow<Result<GameList>> = flow {
        emit(Result.Loading())

        val result = service.getGameList(page, search, RawrService.key)

        if (result.isSuccessful) {
            result.body()?.let {
                emit(Result.Success(it))
            }
        } else {
            emit(Result.Error(message = "An error has occurred"))
        }
    }

    override fun getGame(id: Int): Flow<Result<Game>> = flow {
        val cachedGame = dao.getBookmarkedGame(id)
        emit(Result.Loading(cachedGame))

        val result = service.getGame(id, RawrService.key)

        if (result.isSuccessful) {
            result.body()?.let {
                val data = if (cachedGame != null) {
                    it.copy(isBookmarked = true)
                } else {
                    it.copy(isBookmarked = false)
                }
                emit(Result.Success(data))
            }
        } else {
            emit(Result.Error(message = "An error has occurred"))
        }
    }
}