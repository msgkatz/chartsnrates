package com.msgkatz.ratesapp

import com.msgkatz.ratesapp.data.network.rest.PlatformInfoDTApiModel
import com.msgkatz.ratesapp.data.network.rest.PriceSimpleDTApiModel

actual suspend fun getLocalPlatformInfo(): Result<PlatformInfoDTApiModel> {
    TODO("Not yet implemented")
}

actual suspend fun getLocalPriceSimple(): Result<List<PriceSimpleDTApiModel>> {
    TODO("Not yet implemented")
}