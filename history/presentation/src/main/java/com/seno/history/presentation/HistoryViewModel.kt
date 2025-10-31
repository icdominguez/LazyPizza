package com.seno.history.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class HistoryViewModel : ViewModel() {
    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    private val _event = Channel<HistoryEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: HistoryAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }
}
