package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.Interval
import kotlinx.coroutines.flow.Flow

interface IntervalListRepository {
    suspend fun getIntervals(): List<Interval>?
    suspend fun getIntervalByName(interval: String) : Interval?

    fun getIntervalsAsFlow(): Flow<List<Interval>?>
    fun getIntervalByNameAsFlow(interval: String) : Flow<Interval?>
}