package com.seno.history.presentation

sealed interface HistoryAction {
    data object OnSingInClick : HistoryAction

    data object OnGoToMenuClick : HistoryAction
}
