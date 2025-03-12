package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.Tool
import kotlinx.coroutines.flow.Flow

interface AssetRepository {
    fun getAssets(): Flow<Map<String, Tool>?>
}