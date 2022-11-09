package com.test.perqaratest.presentation.game_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.perqaratest.data.utils.Result
import com.test.perqaratest.domain.repository.GameRepository
import com.test.perqaratest.presentation.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val repository: @JvmSuppressWildcards GameRepository,
    private val dispatcher: @JvmSuppressWildcards CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow(GamesState())
    val state get(): StateFlow<GamesState> = _state

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent get() = _uiEvent.asSharedFlow()

    private val handler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch(dispatcher) {
            _uiEvent.emit(UiEvent.ShowSnackbar(exception.message ?: "An error has occurred"))
        }
    }

    private var fooJob: Job? = null
    fun emit(int: Int) {
        fooJob?.cancel()
        fooJob = viewModelScope.launch(dispatcher) {
            delay(500L)
            println(int)
            repository.foo(int)
                .onEach { int ->
                    println(int)
                    _state.update { it.copy(currentPage = int) }
                }
                .launchIn(this)
        }
    }

    init {
        getGames(1)
    }

    fun loadMorePage() {
        if (state.value.allowLoadMore) {
            getGames(state.value.currentPage + 1)
        }
    }

    fun searchGames(search: String) {
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(search = search) }
            getGames(1)
        }
    }

    fun resetSearchGames() {
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(search = null) }
            getGames(1)
        }
    }

    private var job: Job? = null
    fun getGames(page: Int) {
        job?.cancel()
        job = viewModelScope.launch(dispatcher + handler) {
            delay(500L)
            repository.getGameList(page, state.value.search)
                .onEach { result ->
                    println("result "+result.data)
                    when (result) {
                        is Result.Success -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    currentPage = page,
                                    search = state.value.search,
                                    games = if (page == 1) {
                                        result.data?.results ?: emptyList()
                                    } else {
                                        state.value.games + (result.data?.results ?: emptyList())
                                    },
                                    allowLoadMore = (result.data?.results ?: emptyList()).isNotEmpty()
                                )
                            }
                        }
                        is Result.Error -> {
                            _state.update { it.copy(isLoading = false) }
                        }
                        is Result.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }
                    }
                }
                .launchIn(this)
        }
    }
}