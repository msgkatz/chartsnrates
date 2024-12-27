package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.Asset
import com.msgkatz.ratesapp.data.model.PlatformInfo
import com.msgkatz.ratesapp.data.model.Tool
import kotlinx.coroutines.flow.Flow

interface ToolRepository
{
 suspend fun getPlatformInfo(): PlatformInfo?
 suspend fun getToolMap() : Map<String, Tool>?
 suspend fun getQuoteAssetMap() : Map<String, Asset>?
 suspend fun getQuoteAssetSet() : Set<Asset>?

 fun getPlatformInfoAsFlow(): Flow<PlatformInfo?>
 fun getToolMapAsFlow() : Flow<Map<String, Tool>?>
 fun getQuoteAssetMapAsFlow() : Flow<Map<String, Asset>?>
 fun getQuoteAssetSetAsFlow() : Flow<Set<Asset>?>



}