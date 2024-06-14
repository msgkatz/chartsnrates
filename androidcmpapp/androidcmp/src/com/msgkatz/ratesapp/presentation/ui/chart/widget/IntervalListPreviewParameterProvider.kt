package com.msgkatz.ratesapp.presentation.ui.chart.widget

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.msgkatz.ratesapp.domain.entities.Interval
import com.msgkatz.ratesapp.utils.Parameters

class IntervalListPreviewParameterProvider: PreviewParameterProvider<List<Interval>> {
    override val values : Sequence<List<Interval>>
        get() = sequenceOf(
            Parameters.defaulScaletList
        )
}