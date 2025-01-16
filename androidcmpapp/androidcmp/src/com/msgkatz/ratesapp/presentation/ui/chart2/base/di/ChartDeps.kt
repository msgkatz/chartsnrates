package com.msgkatz.ratesapp.presentation.ui.chart2.base.di

import android.content.Context
import com.msgkatz.ratesapp.di.app.AppModule
import com.msgkatz.ratesapp.domain.interactors.GetCurrentPrice
import com.msgkatz.ratesapp.domain.interactors.GetCurrentPricesInterim
import com.msgkatz.ratesapp.domain.interactors.GetIntervalByName
import com.msgkatz.ratesapp.domain.interactors.GetIntervals
import com.msgkatz.ratesapp.domain.interactors.GetPriceHistory
import com.msgkatz.ratesapp.domain.interactors.GetTools
import com.msgkatz.ratesapp.presentation.common.messaging.IRxBus
import javax.inject.Named
import kotlin.properties.Delegates.notNull

interface ChartDeps {
    val mGetTools: GetTools
    val mGetIntervals: GetIntervals
    val rxBus: IRxBus

    //@Named(AppModule.APP_CONTEXT)
    val appContext: Context
    val mGetPriceHistory: GetPriceHistory
    val mGetCurrentPricesInterim: GetCurrentPricesInterim
    val mGetCurrentPrice: GetCurrentPrice
    val mGetIntervalByName: GetIntervalByName
}

interface ChartDepsProvider {
    val deps: ChartDeps
    companion object: ChartDepsProvider by ChartDepsStore
}

object ChartDepsStore: ChartDepsProvider {
    override var deps: ChartDeps by notNull()
}