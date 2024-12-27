package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.PriceSimple

interface ToolListPriceRepository {

    //public Flowable<Optional<Map<String, Set<PriceSimple>>>> getToolPrices()
    suspend fun getToolPrices(): Map<String, Set<PriceSimple>>
    suspend fun updateMultiMap(newmap: MutableMap<String, Set<PriceSimple>>)
}