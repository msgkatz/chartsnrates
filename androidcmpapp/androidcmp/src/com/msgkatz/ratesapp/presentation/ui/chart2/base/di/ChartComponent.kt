package com.msgkatz.ratesapp.presentation.ui.chart2.base.di

import com.msgkatz.ratesapp.presentation.ui.chart2.widget.ChartActivityNew
import com.msgkatz.ratesapp.presentation.ui.chart2.widget.ChartGdxFragmentNew
import dagger.Component

@[Component(dependencies = [ChartDeps::class])]
/**internal**/ interface ChartComponent {
    fun inject(chartActivityNew: ChartActivityNew)
    fun inject(chartGdxFragmentNew: ChartGdxFragmentNew)

    @Component.Builder
    interface Builder {
        fun deps(deps: ChartDeps): Builder
        fun build(): ChartComponent
    }
}