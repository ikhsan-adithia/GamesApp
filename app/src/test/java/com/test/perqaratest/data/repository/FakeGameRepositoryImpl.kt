package com.test.perqaratest.data.repository

import com.test.perqaratest.data.models.Game
import com.test.perqaratest.data.models.GameItem
import com.test.perqaratest.data.models.GameList
import com.test.perqaratest.data.utils.Result
import com.test.perqaratest.domain.repository.GameRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeGameRepositoryImpl: GameRepository {

    companion object {
        fun getDummyDataGameList() = GameList(
            count = 2,
            results = listOf(
                GameItem(
                    added = 4000,
                    rating = 5.0,
                    id = 123,
                    released = "2020-10-23",
                    backgroundImage = "",
                    name = "GTA V"
                ),
                GameItem(
                    added = 8000,
                    rating = 4.7,
                    id = 124,
                    released = "2000-10-23",
                    backgroundImage = "",
                    name = "GTA San Andreas"
                )
            ))
    }

    private var isSuccess = true

    fun setSuccess(state: Boolean) {
        isSuccess = state
    }

    override fun foo(int: Int): Flow<Int> = flow {
        emit(0)
        delay(100L)
        emit(int)
    }

    override fun getGameList(page: Int, search: String?): Flow<Result<GameList>> = flow {
        emit(Result.Loading())

        val result = getDummyDataGameList()

        if (isSuccess) {
            emit(Result.Success(result))
        } else {
            emit(Result.Error(message = "An error has occurred"))
        }
    }

    override fun getGame(id: Int): Flow<Result<Game>> {
        TODO("Not yet implemented")
    }

    override suspend fun addToBookmark(game: Game) {
        TODO("Not yet implemented")
    }

    override suspend fun removeBookmark(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getBookmarkedGames(): Flow<List<Game>> {
        TODO("Not yet implemented")
    }
}