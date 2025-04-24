package com.msgkatz.ratesapp.core.uikitkmp.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun getAndroidColorScheme(): ColorScheme {
    if (supportsDynamicTheming()) {
        val darkTheme: Boolean = isSystemInDarkTheme()
        val context = LocalContext.current
        return if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else return darkColorScheme()

}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
actual fun couldUseAndroidTheme(): Boolean {
    return if (supportsDynamicTheming()) true else false
}