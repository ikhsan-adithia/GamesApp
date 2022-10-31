package com.test.perqaratest.presentation.game_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.perqaratest.data.models.Game
import com.test.perqaratest.data.utils.Result
import com.test.perqaratest.domain.repository.GameRepository
import com.test.perqaratest.presentation.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class GameDetailViewModel @Inject constructor(
    private val repository: GameRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow<Game?>(null)
    val state get(): StateFlow<Game?> = _state

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent get() = _uiEvent.asSharedFlow()

    init {
        savedStateHandle.get<Int>("id")?.let { id ->
            getDetailGame(id)
        }
    }

    fun toggleBookmark() {
        state.value?.isBookmarked?.let { state ->
            if (state) {
                removeBookmark()
            } else {
                addToBookmark()
            }
        }
    }

    private fun addToBookmark() {
        viewModelScope.launch {
            state.value?.let { game ->
                repository.addToBookmark(game)
                _state.update { it?.copy(isBookmarked = true) }
            }
        }
    }

    private fun removeBookmark() {
        viewModelScope.launch {
            state.value?.id?.let { id ->
                repository.removeBookmark(id)
                _state.update { it?.copy(isBookmarked = false) }
            }
        }
    }

    private fun getDetailGame(id: Int) {
        repository.getGame(id)
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _state.update { result.data }
                    }
                    is Result.Error -> {
                        _state.update { null }
                    }
                    is Result.Loading -> {
                        _state.update { null }
                    }
                }
            }
            .launchIn(viewModelScope + Dispatchers.IO)
    }
}