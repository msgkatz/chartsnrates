package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.Interval
import kotlinx.coroutines.flow.Flow

interface IntervalListRepository {
    fun getIntervals(): Flow<List<Interval>?>
    fun getIntervalByName(interval: String) : Flow<Interval?>
}