package com.msgkatz.ratesapp.data.repos.composite

import com.msgkatz.ratesapp.data.model.Candle
import com.msgkatz.ratesapp.data.model.Interval
import com.msgkatz.ratesapp.data.repos.base.CurToolRealtimeBalancedPriceRepository
import com.msgkatz.ratesapp.data.repos.base.CurToolRealtimePriceRepository
import com.msgkatz.ratesapp.data.repos.base.HistoricalPriceRepository
import com.msgkatz.ratesapp.data.repos.base.IntervalListRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

class CurrentToolPriceRepositoryImpl(
    //private val map: MutableMap<String, MutableSet<Candle>>,
    private val intervalListRepository: IntervalListRepository,
    private val historicalPriceRepository: HistoricalPriceRepository,
    private val curToolRealtimePriceRepository: CurToolRealtimePriceRepository,
    private val curToolRealtimeBalancedPriceRepository: CurToolRealtimeBalancedPriceRepository,
    private val curToolRealtimeBalancedPriceRepositoryV2: CurToolRealtimeBalancedPriceRepository,
): CurrentToolPriceRepository {
    //private val map: MutableMap<String, MutableSet<Candle>> = HashMap()
    override suspend fun getPriceHistoryByTool(
        symbol: String,
        interval: String,
        startTime: Long,
        endTime: Long,
        limit: Int
    ): List<Candle>? {
        return historicalPriceRepository.getData(symbol, interval, startTime, endTime, limit)
    }

    override suspend fun getPricesInterimByTool(
        symbol: String,
        interval: String,
        startTime: Long
    ): List<Candle>? = coroutineScope {
        val _interval: Interval = intervalListRepository.getIntervalByName(interval) ?: throw Exception("No interval")
        val repo = if (_interval.type == 0) curToolRealtimeBalancedPriceRepository else curToolRealtimeBalancedPriceRepositoryV2
        repo.getInterimPrices(symbol, interval, startTime, null)
    }

    override suspend fun getToolRealtimePrice(symbol: String, interval: String): Flow<Candle> {
        val _interval: Interval = intervalListRepository.getIntervalByName(interval) ?: throw Exception("No interval")
        val repo = if (_interval.type == 0) curToolRealtimeBalancedPriceRepository else curToolRealtimeBalancedPriceRepositoryV2
        return repo.subscribeToolPrice(symbol, interval)
    }

    override suspend fun getToolRealtimePriceCombo(
        symbol: String,
        interval: String
    ): Flow<Candle?> {
        return curToolRealtimePriceRepository.subscribeToolPriceCombo(symbol, interval)
    }


}