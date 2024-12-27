package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.Candle
import com.msgkatz.ratesapp.data.model.Interval
import com.msgkatz.ratesapp.data.network.rest.PriceByCandleApiModel
import com.msgkatz.ratesapp.data.network.rest.RestDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.floor

class HistoricalPriceRepositoryImpl(
    private val networkds: RestDataSource,
    private val map: MutableMap<String, MutableSet<Candle>>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val intervalsRepository: IntervalListRepository,
) : HistoricalPriceRepository {

    private var curset: MutableSet<Candle>? = null //LinkedHashSet()

    override fun getDataAsFlow(
        symbol: String,
        interval: String,
        startTime: Long,
        endTime: Long,
        limit: Int
    ): Flow<List<Candle>?> = flow {
        getData(symbol, interval, startTime, endTime, limit)
    }
    override suspend fun getData(symbol: String,
                        interval: String,
                        startTime: Long,
                        endTime: Long,
                        limit: Int
    ): List<Candle>? = coroutineScope {
        val _interval: Interval = intervalsRepository.getIntervalByName(interval) ?: return@coroutineScope null
        val pbc = networkds.getPriceByCandle(symbol, interval, startTime, endTime, limit)
        if (pbc.isFailure || pbc.getOrNull() == null) {
            return@coroutineScope null
        } else {
            val list : MutableList<Candle> = ArrayList()
            pbc.getOrNull()?.map {
                it.toCandleList(_interval, startTime, endTime)?.let {
                    list.addAll(it)
                }
            }
            list
        }
    }

}

fun List<String>.toCandleList(interval: Interval, startTime: Long, endTime: Long) : List<Candle>? {
    if (this.size < 7) return null
    try {
        val priceByCandle = PriceByCandleApiModel(
            openTime = this[0].toLong(),
            open = this[1].toDouble(),
            high = this[2].toDouble(),
            low = this[3].toDouble(),
            close = this[4].toDouble(),
            closeTime = this[6].toLong(),
        )

        val list : MutableList<Candle> = ArrayList()
        val delta: Long = priceByCandle.closeTime - priceByCandle.openTime + 1
        if (delta == interval.perItemDefaultMs) {
            /** not using normalization, because openTime is normalized already **/
            if (priceByCandle.openTime < endTime)
                list.add(
                    Candle(
                        priceOpen = priceByCandle.open,
                        priceHigh = priceByCandle.high,
                        priceLow = priceByCandle.low,
                        priceClose = priceByCandle.close,
                        time = priceByCandle.openTime,
                    )
                )
        } else if (delta > interval.perItemDefaultMs) {
            val itemCount = floor((delta / interval.perItemDefaultMs).toDouble()).toInt()
            for (i in 0 until itemCount) {
                val newTime: Long =
                    priceByCandle.openTime + (i * interval.perItemDefaultMs)
                if ((newTime < endTime) && (newTime <= priceByCandle.closeTime)) {
                    list.add(
                        Candle(
                            priceOpen = priceByCandle.open,
                            priceHigh = priceByCandle.high,
                            priceLow = priceByCandle.low,
                            priceClose = priceByCandle.close,
                            time = newTime,
                        )
                    )
                }
            }
        } else {
            throw Exception("Something went wrong w/ historical data processing")
        }

        return list

    } catch (e: Exception) {
        println(e.toString())
        return null
    }
}