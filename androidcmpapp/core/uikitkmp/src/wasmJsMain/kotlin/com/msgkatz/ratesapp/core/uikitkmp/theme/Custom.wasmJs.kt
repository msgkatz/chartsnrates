package com.msgkatz.ratesapp.core.uikitkmp.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun getAndroidColorScheme(): ColorScheme {
    return darkColorScheme()

}

actual fun couldUseAndroidTheme(): Boolean {
    return false
}