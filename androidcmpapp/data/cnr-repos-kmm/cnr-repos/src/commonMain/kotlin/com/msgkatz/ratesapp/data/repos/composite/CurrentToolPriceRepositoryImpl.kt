package com.msgkatz.ratesapp.data.repos.composite

import com.msgkatz.ratesapp.data.model.Candle

class CurrentToolPriceRepositoryImpl {
    private val map: MutableMap<String, MutableSet<Candle>> = HashMap()
}