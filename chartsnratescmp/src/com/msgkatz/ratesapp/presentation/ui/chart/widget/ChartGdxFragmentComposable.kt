package com.msgkatz.ratesapp.presentation.ui.chart.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.msgkatz.ratesapp.databinding.FragmentChartGdxForComposeBinding
import com.msgkatz.ratesapp.databinding.FragmentChartGdxOldForComposeBinding

@Composable
fun ChartGdxFragmentNewInCompose(
    modifier: Modifier = Modifier,
) {
    AndroidViewBinding(
        modifier = modifier,
        factory = FragmentChartGdxForComposeBinding::inflate) {
        val myFragment = fragmentContainerView.getFragment<ChartGdxFragmentNew>()
        // ...
    }
}

//@Composable
//fun ChartGdxFragmentInCompose(
//    modifier: Modifier = Modifier,
//) {
//    AndroidViewBinding(
//        modifier = modifier,
//        factory = FragmentChartGdxOldForComposeBinding::inflate) {
//        val myFragment = fragmentOldContainerView.getFragment<ChartGdxFragment>()
//        // ...
//    }
//}