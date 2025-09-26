package com.msgkatz.ratesapp

import com.msgkatz.ratesapp.data.model.Asset
import com.msgkatz.ratesapp.data.model.Candle
import com.msgkatz.ratesapp.data.model.PlatformInfo
import com.msgkatz.ratesapp.data.model.PriceSimple
import com.msgkatz.ratesapp.data.network.rest.RestController
import com.msgkatz.ratesapp.data.network.rest.RestDataSource
import com.msgkatz.ratesapp.data.network.websocket.WebSocketController3rd
import com.msgkatz.ratesapp.data.network.websocket.WebSocketDataSource
import com.msgkatz.ratesapp.data.repos.base.AssetRepository
import com.msgkatz.ratesapp.data.repos.base.AssetRepositoryImpl
import com.msgkatz.ratesapp.data.repos.base.CurToolRealtimeBalancedPriceRepository
import com.msgkatz.ratesapp.data.repos.base.CurToolRealtimeBalancedPriceRepositoryImpl
import com.msgkatz.ratesapp.data.repos.base.CurToolRealtimeBalancedV2PriceRepositoryImpl
import com.msgkatz.ratesapp.data.repos.base.CurToolRealtimePriceRepository
import com.msgkatz.ratesapp.data.repos.base.CurToolRealtimePriceRepositoryImpl
import com.msgkatz.ratesapp.data.repos.base.HistoricalPriceRepository
import com.msgkatz.ratesapp.data.repos.base.HistoricalPriceRepositoryImpl
import com.msgkatz.ratesapp.data.repos.base.IntervalListRepository
import com.msgkatz.ratesapp.data.repos.base.IntervalListRepositoryImpl
import com.msgkatz.ratesapp.data.repos.base.LocalJsonDataSource
import com.msgkatz.ratesapp.data.repos.base.LocalJsonDataSourceImpl
import com.msgkatz.ratesapp.data.repos.base.ToolListPriceRepository
import com.msgkatz.ratesapp.data.repos.base.ToolListPriceRepositoryImpl
import com.msgkatz.ratesapp.data.repos.base.ToolListRealtimesPriceRepository
import com.msgkatz.ratesapp.data.repos.base.ToolListRealtimesPriceRepositoryImpl
import com.msgkatz.ratesapp.data.repos.base.ToolRepository
import com.msgkatz.ratesapp.data.repos.base.ToolRepositoryImpl
import com.msgkatz.ratesapp.data.repos.composite.CurrentToolPriceRepository
import com.msgkatz.ratesapp.data.repos.composite.CurrentToolPriceRepositoryImpl
import com.msgkatz.ratesapp.feature.rootkmp.main.QuoteAssetDataKeeper
import com.msgkatz.ratesapp.feature.rootkmp.splash.SplashDataKeeper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow

class TmpDataKeeper(
    private val coroutineScope: CoroutineScope? = null,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Unconfined,
): SplashDataKeeper, QuoteAssetDataKeeper {
    val ceh = CoroutineExceptionHandler { coroutineContext, throwable ->
        println(throwable.message ?: throwable.toString())
    }
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default + ceh)
    /** ******
     * data sources
     * ******/
    val localJsonDataSource: LocalJsonDataSource = LocalJsonDataSourceImpl()
    val restDataSource: RestDataSource =
        LocalRestController(
        //RestController(
        coroutineScope = coroutineScope ?: scope,
        ioDispatcher = ioDispatcher
    )
    val webSocketDataSource: WebSocketDataSource =
        WebSocketController3rd(
        //WebSocketController(
        coroutineScope = coroutineScope?: scope,
        ioDispatcher = ioDispatcher,
        debug = true //false //true
    )

    /** ******
     * base repos
     * ******/
    val toolRepository: ToolRepository = ToolRepositoryImpl(
        networkds = restDataSource,
        ioDispatcher = ioDispatcher
    )
    val assetRepository: AssetRepository = AssetRepositoryImpl(
        networkds = restDataSource,
        tools = toolRepository,
        ioDispatcher = ioDispatcher
    )

    val intervalListRepository: IntervalListRepository = IntervalListRepositoryImpl(
        ioDispatcher = ioDispatcher
    )
    val toolListPriceRepository: ToolListPriceRepository = ToolListPriceRepositoryImpl(
        networkds = restDataSource,
        toolRepository = toolRepository,
        ioDispatcher = ioDispatcher
    )
    val toolListRealtimesPriceRepository: ToolListRealtimesPriceRepository = ToolListRealtimesPriceRepositoryImpl(
        wsockds = webSocketDataSource,
        toolRepository = toolRepository,
        toolListPriceRepository = toolListPriceRepository,
        ioDispatcher = ioDispatcher
    )
    val historicalPriceRepository: HistoricalPriceRepository = HistoricalPriceRepositoryImpl(
        networkds = restDataSource,
        intervalListRepository = intervalListRepository
    )

    private val map: MutableMap<String, MutableSet<Candle>> = mutableMapOf()
    val curToolRealtimeBalancedPriceRepository: CurToolRealtimeBalancedPriceRepository = CurToolRealtimeBalancedPriceRepositoryImpl(
        wsockds = webSocketDataSource,
        map = map,
        ioDispatcher = ioDispatcher
    )
    val curToolRealtimeBalancedPriceRepositoryV2: CurToolRealtimeBalancedPriceRepository = CurToolRealtimeBalancedV2PriceRepositoryImpl(
        wsockds = webSocketDataSource,
        map = map,
        intervalListRepository = intervalListRepository,
        ioDispatcher = ioDispatcher
    )
    val curToolRealtimePriceRepository: CurToolRealtimePriceRepository = CurToolRealtimePriceRepositoryImpl(
        wsockds = webSocketDataSource,
        map = map
    )

    val currentToolPriceRepository: CurrentToolPriceRepository = CurrentToolPriceRepositoryImpl(
        intervalListRepository = intervalListRepository,
        historicalPriceRepository = historicalPriceRepository,
        curToolRealtimePriceRepository = curToolRealtimePriceRepository,
        curToolRealtimeBalancedPriceRepository = curToolRealtimeBalancedPriceRepository,
        curToolRealtimeBalancedPriceRepositoryV2 = curToolRealtimeBalancedPriceRepositoryV2
    )


    /** ****** ****** ****** ****** ****** ****** ****** ****** ******
     * use cases:
     * ****** ****** ****** ****** ****** ****** ****** ****** ******/

    /** ******
     * use cases splash
     * ******/
    override suspend fun getPlatformInfo(): PlatformInfo?
        = toolRepository.getPlatformInfo()


    /** ******
     * use cases quote asset
     * ******/
    override suspend fun getQuoteAssetMap() : Map<String, Asset>?
        = toolRepository.getQuoteAssetMap()

    override suspend fun getToolPrices(): Map<String, Set<PriceSimple>>
        = toolListPriceRepository.getToolPrices()

    override fun subscribeToolPrices(): Flow<Map<String, Set<PriceSimple>>>
        = toolListRealtimesPriceRepository.subscribeToolPrices()


}