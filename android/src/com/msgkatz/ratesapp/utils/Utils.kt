package com.msgkatz.ratesapp.utils

import android.content.Context
import com.msgkatz.ratesapp.R

fun isTablet(context: Context): Boolean {
    return context.resources.getBoolean(R.bool.isTablet)
}