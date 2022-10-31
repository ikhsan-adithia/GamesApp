package com.test.perqaratest.presentation.utils

sealed class UiEvent {
    data class ShowSnackbar(val message: String): UiEvent()
}
