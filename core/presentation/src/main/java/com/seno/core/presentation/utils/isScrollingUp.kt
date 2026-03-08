package com.seno.core.presentation.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.snapshotFlow

@Composable
internal fun LazyListState.isScrollingUp(): State<Boolean> {
    return produceState(initialValue = true) {
        var lastIndex = 0
        var lastScroll = Int.MAX_VALUE
        snapshotFlow {
            firstVisibleItemIndex to firstVisibleItemScrollOffset
        }.collect { (currentIndex, currentScroll) ->
            value = when {
                currentIndex != lastIndex -> currentIndex < lastIndex
                else -> currentScroll < lastScroll
            }
            lastIndex = currentIndex
            lastScroll = currentScroll
        }
    }
}