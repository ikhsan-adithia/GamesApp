package com.test.perqaratest.presentation.game_list

import com.test.perqaratest.MainDispatcherRule
import com.test.perqaratest.data.repository.FakeGameRepositoryImpl
import com.test.perqaratest.utils.DispatcherProvider
import com.test.perqaratest.utils.StandardDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GamesViewModelTest {

    private lateinit var repository: FakeGameRepositoryImpl
    private lateinit var viewModel: GamesViewModel
    private lateinit var dispatcher: DispatcherProvider

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        dispatcher = StandardDispatchers()

        repository = FakeGameRepositoryImpl()
        viewModel = GamesViewModel(repository, dispatcher.io)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test getGames`() = runTest {
        val first = viewModel.state.value
        println(first)
        viewModel.getGames(1)
        viewModel.emit(4)
        val second = viewModel.state.value
        println(second)

        advanceUntilIdle()
        val third = viewModel.state.value
        println(third)

//        val result = mutableListOf<GamesState>()
//
//        viewModel.getGames(1)
//        advanceUntilIdle()
//
//        val job = launch {
//            viewModel.state.toList(result)
//        }
//
//        println(result)
//
//        job.cancel()

//        val expected = GamesState()
//        assert(state.games.size == expected.games.size)
//        assert(state.allowLoadMore == expected.allowLoadMore)
//        assert(state.isLoading == expected.isLoading)
//        assert(state.search == expected.search)
//        assert(state.currentPage == expected.currentPage)
    }
}