package com.msgkatz.ratesapp.feature.chartgdx.widget

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.msgkatz.ratesapp.old.domain.entities.IntervalJava
import com.msgkatz.ratesapp.old.utils.Parameters


class IntervalListPreviewParameterProvider: PreviewParameterProvider<List<IntervalJava>> {
    override val values : Sequence<List<IntervalJava>>
        get() = sequenceOf(
            Parameters.defaulScaletList
        )
}