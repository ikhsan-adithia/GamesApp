package com.test.perqaratest.data.repository

import com.test.perqaratest.data.local.GameDao
import com.test.perqaratest.data.models.Game
import com.test.perqaratest.data.models.GameList
import com.test.perqaratest.data.remote.RawrService
import com.test.perqaratest.data.utils.Result
import com.test.perqaratest.domain.repository.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val service: RawrService,
    private val dao: GameDao,
    private val dispatcher: CoroutineDispatcher
): GameRepository {

    override fun foo(int: Int): Flow<Int> = flow {
        emit(0)
        delay(100L)
        emit(int)
    }.flowOn(dispatcher)

    override suspend fun addToBookmark(game: Game) {
        dao.addToBookmark(game.copy(isBookmarked = true))
    }

    override suspend fun removeBookmark(id: Int) {
        dao.removeBookmark(id)
    }

    override fun getBookmarkedGames(): Flow<List<Game>> =
        dao.getBookmarkedGames().flowOn(dispatcher)

    override fun getGameList(page: Int, search: String?): Flow<Result<GameList>> = flow {
        emit(Result.Loading())

        val result = service.getGameList(page, search, RawrService.key)

        if (result.isSuccessful) {
            result.body()?.let {
                emit(Result.Success(it))
            } ?: emit(Result.Error(message = "An error has occurred"))
        } else {
            emit(Result.Error(message = "An error has occurred"))
        }
    }.flowOn(dispatcher)

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
            } ?: emit(Result.Error(message = "An error has occurred"))
        } else {
            emit(Result.Error(message = "An error has occurred"))
        }
    }.flowOn(dispatcher)
}