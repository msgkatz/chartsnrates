package com.msgkatz.ratesapp

import com.msgkatz.ratesapp.data.network.rest.PlatformInfoDTApiModel
import com.msgkatz.ratesapp.data.network.rest.PriceSimpleDTApiModel

class RestExtras {
}

expect suspend fun getLocalPlatformInfo(): Result<PlatformInfoDTApiModel>
expect suspend fun getLocalPriceSimple(): Result<List<PriceSimpleDTApiModel>>