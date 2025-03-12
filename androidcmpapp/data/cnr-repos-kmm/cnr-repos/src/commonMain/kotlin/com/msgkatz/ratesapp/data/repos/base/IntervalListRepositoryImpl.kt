package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.Interval
import com.msgkatz.ratesapp.data.model.IntervalLocalJSON
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class IntervalListRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
): IntervalListRepository {
    private val jssource : LocalJsonDataSource = LocalJsonDataSourceImpl(ioDispatcher)
    private val mutex: Mutex = Mutex()
    private var data : MutableMap<String, Interval>? = null
    private var sortedIntervalList : MutableList<Interval>? = null
    private var isEmpty : Boolean = data == null

    val exh : CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("ToolRepositoryImpl err: ${throwable.message ?: throwable.toString()}")
    }
    val scope : CoroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher + exh)

    override fun getIntervalsAsFlow(): Flow<List<Interval>?> = flow {
        getIntervals()
    }

    override fun getIntervalByNameAsFlow(interval: String) : Flow<Interval?> = flow {
        getIntervalByName(interval)
    }

    override suspend fun getIntervals(): List<Interval>? = coroutineScope {
        if (isEmpty()) {
            if (update()) {
                sortedIntervalList
            } else {
                null
            }
        } else {
            sortedIntervalList
        }
    }
    override suspend fun getIntervalByName(interval: String) : Interval? = coroutineScope {
        if (isEmpty()) {
            if (update()) {
                data?.getOrElse(interval) { null }
            } else {
                null
            }
        } else {
            data?.getOrElse(interval) { null }
        }
    }

    private suspend fun update(): Boolean = scope.async {
        mutex.withLock {
            var retVal = false
            try {
                val result = jssource.getLocalJsonIntervals()
                if (result.isSuccess) {
                    result.getOrNull()?.map {
                            it.toEntity()
                        }
                        ?.sortedBy { it.id }
                        ?.map {
                            if (data == null) data = HashMap()
                            it.symbol?.let { sym -> data?.put(sym, it)}
                            sortedIntervalList?.add(it)
                        }?.let { retVal = it.size > 0  }
                }
            } catch (e: Exception) {
                println("IntervalListRepositoryImpl err: ${e.message ?: e.toString()}")
            }
            retVal
        }
    }.await()

    private suspend fun isEmpty() : Boolean =
        scope.async {
            mutex.withLock { isEmpty }
        }.await()
}


fun IntervalLocalJSON.toEntity(): Interval {
    return Interval(id, type, symbol, symbolApi, perItemDefaultMs, perBlockDefaultMs, perItemMinMs, perBlockMinMs, perItemMaxMs, perBlockMaxMs, inUse)
}