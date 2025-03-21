package com.msgkatz.ratesapp.feature.chartgdx.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.msgkatz.ratesapp.feature.chartgdx.databinding.FragmentChartGdxForCompose2Binding



@Composable
fun ChartGdxFragmentNewInCompose(
    modifier: Modifier = Modifier,
) {
    AndroidViewBinding(
        modifier = modifier,
        factory = FragmentChartGdxForCompose2Binding::inflate) {
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