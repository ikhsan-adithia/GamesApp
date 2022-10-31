package com.test.perqaratest.presentation.game_list

import com.test.perqaratest.data.models.GameItem

data class GamesState(
    val isLoading: Boolean = false,
    val allowLoadMore: Boolean = true,
    val currentPage: Int = 1,
    val search: String? = null,
    val games: List<GameItem> = emptyList()
)
