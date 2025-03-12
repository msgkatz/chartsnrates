package com.msgkatz.ratesapp.data.model

import kotlin.math.floor

class Extensions {
}

fun normalizeInSeconds(delta: Int, unixTime: Long): Long {
    return ((floor(
        (unixTime / (delta.toLong())).toDouble()
    ).toLong()
            )
            *
            (delta.toLong()))
}

fun normalizeInSeconds(delta: Long, unixTime: Long): Long {
    return ((floor(
        (unixTime / (delta.toLong())).toDouble()
    ).toLong()
            )
            *
            (delta.toLong()))
}