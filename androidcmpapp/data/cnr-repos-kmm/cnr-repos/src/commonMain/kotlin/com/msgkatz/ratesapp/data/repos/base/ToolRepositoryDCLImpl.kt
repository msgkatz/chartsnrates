package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.Asset
import com.msgkatz.ratesapp.data.model.PlatformInfo
import com.msgkatz.ratesapp.data.model.Tool
import com.msgkatz.ratesapp.data.network.rest.RestDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Double Check Locking version
 */
class ToolRepositoryDCLImpl(
    private val networkds: RestDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ToolRepository {
    private val defDispatcher: CoroutineDispatcher = Dispatchers.Default
    private val mutex: Mutex = Mutex()
    private var data : PlatformInfo? = null
    private var toolMap: MutableMap<String, Tool> = HashMap()
    private var quoteAssetSet: MutableSet<Asset> = HashSet()
    private var quoteAssetMap: MutableMap<String, Asset> = HashMap()

    private val isEmpty : Boolean
            get() = data == null

    private val exh : CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("ToolRepositoryImpl err: ${throwable.message ?: throwable.toString()}")
    }

    private val scope : CoroutineScope = CoroutineScope(SupervisorJob() + defDispatcher + exh)

    override suspend fun getPlatformInfo(): PlatformInfo? = coroutineScope {
        check()
        data
    }

    override suspend fun getToolMap(): Map<String, Tool>? = coroutineScope {
        check()
        toolMap
    }

    override suspend fun getQuoteAssetMap(): Map<String, Asset>? = coroutineScope {
        check()
        quoteAssetMap
    }

    override suspend fun getQuoteAssetSet(): Set<Asset>? = coroutineScope {
        check()
        quoteAssetSet
    }

    override fun getPlatformInfoAsFlow(): Flow<PlatformInfo?> = flow {
        emit(getPlatformInfo())
    }.flowOn(defDispatcher)

    override fun getToolMapAsFlow(): Flow<Map<String, Tool>?> = flow {
        emit(getToolMap())
    }.flowOn(defDispatcher)

    override fun getQuoteAssetMapAsFlow(): Flow<Map<String, Asset>?> = flow {
        emit(getQuoteAssetMap())
    }.flowOn(defDispatcher)

    override fun getQuoteAssetSetAsFlow(): Flow<Set<Asset>?> = flow {
        emit(getQuoteAssetSet())
    }.flowOn(defDispatcher)

    /**
     * Double Check Locking
     */
    private suspend fun check() {
        if (isEmpty) {
            mutex.withLock {
                if (isEmpty) {
                    update()
                }
            }
        }
    }

    private suspend fun update(): Boolean = coroutineScope {
        var retVal = false
        try {
            //displayChildren(0, this.coroutineContext.job)
            val pi = networkds.getPlatformInfo()

            if (pi.isFailure) throw Exception("getPlatformInfo failure")
            val curdata = pi.getOrNull()?.toDomain()
            curdata?.let {
                it.toolList
            }

            pi.getOrNull()?.toEntity(toolMap, quoteAssetSet, quoteAssetMap)?.let {
                data = it
                retVal = it != null
            }

        } catch (e: Exception) {
            println("ToolRepositoryDCLImpl err: ${e.message ?: e.toString()}")
        }
        retVal
    }

}