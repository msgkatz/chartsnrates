package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.Asset
import com.msgkatz.ratesapp.data.model.PlatformInfo
import com.msgkatz.ratesapp.data.model.PlatformInfoDTDomain
import com.msgkatz.ratesapp.data.model.Tool
import com.msgkatz.ratesapp.data.network.rest.RestDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex

interface ToolRepository
{
 fun getPlatformInfo(): Flow<PlatformInfo?>
 fun getToolMap() : Flow<Map<String, Tool>?>
 fun getQuoteAssetMap() : Flow<Map<String, Asset>?>
 fun getQuoteAssetSet() : Flow<Set<Asset>?>



}