package com.test.perqaratest.presentation.game_list

import com.test.perqaratest.MainDispatcherRule
import com.test.perqaratest.data.repository.FakeGameRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GamesViewModelTest {

    private lateinit var repository: FakeGameRepositoryImpl
    private lateinit var viewModel: GamesViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        repository = FakeGameRepositoryImpl()
        viewModel = GamesViewModel(repository)
    }

    @Test
    fun `test default GamesState`() {
        val state = viewModel.state.value

        val expected = GamesState()
        assert(state.games.size == expected.games.size)
        assert(state.allowLoadMore == expected.allowLoadMore)
        assert(state.isLoading == expected.isLoading)
        assert(state.search == expected.search)
        assert(state.currentPage == expected.currentPage)
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `test getGames`() = runTest {
//        val result = mutableListOf<GamesState>()
//        viewModel.state.toList(result)
//
//        viewModel.getGames(1)
//        advanceUntilIdle()
//
//        println(result)
//
////        val expected = GamesState()
////        assert(state.games.size == expected.games.size)
////        assert(state.allowLoadMore == expected.allowLoadMore)
////        assert(state.isLoading == expected.isLoading)
////        assert(state.search == expected.search)
////        assert(state.currentPage == expected.currentPage)
//    }
}