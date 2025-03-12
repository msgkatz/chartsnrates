package com.msgkatz.ratesapp.feature.chartgdx.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier



@Composable
fun ChartGdxScreen(
    modifier: Modifier = Modifier,
    //shouldUseNewFragment: Boolean = true,
) {
    //if (shouldUseNewFragment) {
        ChartGdxFragmentNewInCompose(modifier = modifier)
    //} else {
        //ChartGdxFragmentInCompose(modifier = modifier)
    //}
}


