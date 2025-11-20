package com.seno.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seno.core.domain.userdata.UserData
import com.seno.history.domain.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class HistoryViewModel(
    userData: UserData,
    historyRepository: HistoryRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    private val _event = Channel<HistoryEvent>()
    val event = _event.receiveAsFlow()

    init {
        combine(
            userData.getUserId(),
            historyRepository.getHistoryOrders(),
        ) { userId, historyOrders ->
            val isLoggedIn = userId != null
            val mappedOrderItems = if (isLoggedIn) {
                historyOrders.map { it.toOrderItem() }
            } else {
                emptyList()
            }

            _state.update { currentState ->
                currentState.copy(
                    isLoggedIn = isLoggedIn,
                    orderItems = mappedOrderItems,
                    isLoading = false,
                )
            }
        }.flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    fun onAction(action: HistoryAction) {
        when (action) {
            else -> Unit
        }
    }
}
