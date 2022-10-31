package com.test.perqaratest.presentation.game_bookmarked

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.perqaratest.data.models.Game
import com.test.perqaratest.domain.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class GameBookmarkedViewModel @Inject constructor(
    private val repository: GameRepository
): ViewModel() {

    private val _state = MutableStateFlow<List<Game>>(emptyList())
    val state get(): StateFlow<List<Game>> = _state

    init {
        getBookmarkedGames()
    }

    private fun getBookmarkedGames() {
        repository.getBookmarkedGames()
            .onEach { games ->
                _state.update { games }
            }
            .launchIn(viewModelScope + Dispatchers.IO)
    }
}