package com.msgkatz.ratesapp.core.uikitkmp.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable


expect fun couldUseAndroidTheme(): Boolean

@Composable
expect fun getAndroidColorScheme(): ColorScheme