package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.IntervalLocalJSON
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocalJsonDataSourceImpl(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val useAlter: Boolean = false,
): LocalJsonDataSource {
    override suspend fun <T> getLocalJsonData(path: String): Result<T> =
        runCatching {
            null as T
        }

    override suspend fun getLocalJsonAssets(): Result<Unit> =
        runCatching {
            Unit
        }

    override suspend fun getLocalJsonIntervals(): Result<List<IntervalLocalJSON>> =
        if (useAlter)
            getLocalJsonIntervalsAlter()
        else
            getLocalJsonIntervalsWithCtx()

    private suspend fun getLocalJsonIntervalsWithCtx(): Result<List<IntervalLocalJSON>> =
        withContext(ioDispatcher)
        {
            runCatching {
                val data = Json.decodeFromString<List<IntervalLocalJSON>>(LocalJsonData.intervalsData)
                data
            }
        }

    private suspend fun getLocalJsonIntervalsAlter(): Result<List<IntervalLocalJSON>> =
        suspendCoroutine { continuation ->
            val data = runCatching {
                    Json.decodeFromString<List<IntervalLocalJSON>>(LocalJsonData.intervalsData)
                }
            continuation.resume(data)
        }


}