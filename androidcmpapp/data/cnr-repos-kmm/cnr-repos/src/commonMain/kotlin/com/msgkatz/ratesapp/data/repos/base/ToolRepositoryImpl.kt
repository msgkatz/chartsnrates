package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.Asset
import com.msgkatz.ratesapp.data.model.PlatformInfo
import com.msgkatz.ratesapp.data.model.PlatformInfoDTDomain
import com.msgkatz.ratesapp.data.model.Tool
import com.msgkatz.ratesapp.data.model.ToolDTDomain
import com.msgkatz.ratesapp.data.network.rest.PlatformInfoDTApiModel
import com.msgkatz.ratesapp.data.network.rest.RestDataSource
import com.msgkatz.ratesapp.data.network.rest.ToolDTApiModel
import com.msgkatz.ratesapp.data.repos.base.ToolRepositoryImpl.Companion.ACTIVE_TOOL_STATUS
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class ToolRepositoryImpl(
    private val networkds: RestDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ToolRepository {
    private val mutex: Mutex = Mutex()
    private var data : PlatformInfo? = null
    private var toolMap: MutableMap<String, Tool> = HashMap()
    private var quoteAssetSet: MutableSet<Asset> = HashSet()
    private var quoteAssetMap: MutableMap<String, Asset> = HashMap()

    private val isEmpty : Boolean
        get() = data == null

    val exh : CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("ToolRepositoryImpl err: ${throwable.message ?: throwable.toString()}")
    }

    val scope : CoroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher + exh)

    override suspend fun getPlatformInfo(): PlatformInfo? = coroutineScope {
        if (isEmpty()) {
            if (update()) {
                data
            } else {
                null
            }
        } else {
            data
        }
    }
    override fun getPlatformInfoAsFlow() : Flow<PlatformInfo?> = flow {
        emit(getPlatformInfo())
    }.flowOn(ioDispatcher)

    override suspend fun getToolMap() : Map<String, Tool>? = coroutineScope {
        if (isEmpty()) {
            if (update()) {
                toolMap
            } else {
                null
            }
        } else {
            toolMap
        }
    }
    override fun getToolMapAsFlow() : Flow<Map<String, Tool>?> = flow {
        emit(getToolMap())
    }.flowOn(ioDispatcher)

    override suspend fun getQuoteAssetMap() : Map<String, Asset>? = coroutineScope {
        if (isEmpty()) {
            if (update()) {
                quoteAssetMap
            } else {
                null
            }
        } else {
            quoteAssetMap
        }
    }
    override fun getQuoteAssetMapAsFlow() : Flow<Map<String, Asset>?> = flow {
        emit(getQuoteAssetMap())
    }.flowOn(ioDispatcher)

    override suspend fun getQuoteAssetSet() : Set<Asset>? = coroutineScope {
        if (isEmpty()) {
            if (update()) {
                quoteAssetSet
            } else {
                null
            }
        } else {
            quoteAssetSet
        }
    }
    override fun getQuoteAssetSetAsFlow() : Flow<Set<Asset>?> = flow {
        emit(getQuoteAssetSet())
    }.flowOn(ioDispatcher)

    private suspend fun update(): Boolean = scope.async {
        mutex.withLock {
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
                println("ToolRepositoryImpl err: ${e.message ?: e.toString()}")
            }
            retVal
        }
    }.await()

    private fun displayChildren(depth: Int = 0, job: Job) {
        job.children.forEach {
            for (i in 0..depth) {
                print("\t")
            }
            println("child: $it")
            //if (it is Job) {
            displayChildren(depth + 1, it)
            //}
        }
    }


    private suspend fun isEmpty() : Boolean =
        scope.async {
            mutex.withLock { isEmpty }
        }.await()


    companion object {
        const val ACTIVE_TOOL_STATUS: String = "TRADING"
    }

}

fun ToolDTApiModel.toDomain() : ToolDTDomain = run {
    val ret = ToolDTDomain(
        symbol, baseAsset, basePrecision, quoteAsset, quotePrecision, status
    )

    return ret
}

fun PlatformInfoDTApiModel.toDomain() : PlatformInfoDTDomain = run {
    var newtoolList: List<ToolDTDomain>? = null
    val timeZone: String? = null
    val serverTime: Long = 0

    if (toolList != null) {
       newtoolList = ArrayList<ToolDTDomain>()
        for (item in toolList!!) {
            newtoolList.add(item.toDomain())
        }
    }
    val ret = PlatformInfoDTDomain(timeZone, serverTime, newtoolList)
    return ret
}

fun PlatformInfoDTApiModel.toEntity(
    toolMap: MutableMap<String, Tool>,
    quoteAssetSet: MutableSet<Asset>,
    quoteAssetMap: MutableMap<String, Asset>
) : PlatformInfo? = run {
    //var newtoolList: List<Tool>? = null
    val timeZone: String? = timeZone
    val serverTime: Long = serverTime

    if (!toolList.isNullOrEmpty()) {
        //newtoolList = ArrayList<Tool>()
        for (item in toolList!!) {
            val tool = item.toEntity(quoteAssetSet, quoteAssetMap)
            toolMap.put(tool.name, tool)
            //newtoolList.add(tool)
        }

        return PlatformInfo(timeZone, serverTime)
    } else {
        return null
    }

}

fun ToolDTApiModel.toEntity(
    quoteAssetSet: MutableSet<Asset>,
    quoteAssetMap: MutableMap<String, Asset>
) : Tool = run {
    val base: Asset = quoteAssetMap.getOrPut(baseAsset!!) { Asset(-1, baseAsset!!, baseAsset!!) }
    val quote: Asset = quoteAssetMap.getOrElse(quoteAsset!!) { Asset(-1, quoteAsset!!, quoteAsset!!) }

    if (!quoteAssetSet.contains(base)) {
        quoteAssetSet.add(base)
    }
    if (!quoteAssetSet.contains(quote)) {
        quoteAssetSet.add(quote)
    }

    val isActive = ACTIVE_TOOL_STATUS.equals(status, true)
    val ret = Tool(name = symbol!!, baseAsset = base, quoteAsset = quote, isActive = isActive)


    return ret
}