@file:OptIn(ExperimentalCoroutinesApi::class)

package com.seno.lazypizza

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seno.core.domain.userdata.UserData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    userData: UserData,
) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        userData
            .getTotalItemCart()
            .distinctUntilChanged()
            .onEach {
                _state.update { state ->
                    state.copy(totalCartItem = it)
                }
            }.launchIn(viewModelScope)
    }
    fun onAction(event: MainAction) {
        when (event) {
            MainAction.ShowLogoutDialog -> {
                _state.update { it.copy(showLogoutDialog = true) }
            }

            MainAction.DismissLogoutDialog -> {
                _state.update { it.copy(showLogoutDialog = false) }
            }

            MainAction.ConfirmLogout -> {
                viewModelScope.launch {
                    _state.update { it.copy(showLogoutDialog = false) }
                }
            }
        }
    }
}