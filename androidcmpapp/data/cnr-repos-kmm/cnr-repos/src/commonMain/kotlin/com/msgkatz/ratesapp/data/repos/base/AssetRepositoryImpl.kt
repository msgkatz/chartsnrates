package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.Asset
import com.msgkatz.ratesapp.data.model.Tool
import com.msgkatz.ratesapp.data.network.rest.RestDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex

class AssetRepositoryImpl(
    private val networkds: RestDataSource,
    private val tools: ToolRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
): AssetRepository {
    private val mutex: Mutex = Mutex()
    private var data : Map<String, Asset>? = null
    private var isEmpty : Boolean = data == null

    val exh : CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("ToolRepositoryImpl err: ${throwable.message ?: throwable.toString()}")
    }
    val scope : CoroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher + exh)

    override fun getAssets(): Flow<Map<String, Tool>?> = tools.getToolMap()

}