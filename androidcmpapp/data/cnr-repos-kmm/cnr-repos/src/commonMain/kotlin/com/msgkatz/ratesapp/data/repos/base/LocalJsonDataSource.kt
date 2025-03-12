package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.IntervalLocalJSON

interface LocalJsonDataSource {

    suspend fun <T> getLocalJsonData(path: String): Result<T>

    suspend fun getLocalJsonAssets(): Result<Unit>
    suspend fun getLocalJsonIntervals(): Result<List<IntervalLocalJSON>>
}