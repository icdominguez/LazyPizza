package com.seno.history.domain

import com.seno.history.domain.model.HistoryOrder
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getHistoryOrders(): Flow<List<HistoryOrder>>
}
