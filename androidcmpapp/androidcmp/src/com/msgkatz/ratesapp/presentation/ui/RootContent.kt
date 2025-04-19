package com.msgkatz.ratesapp.presentation.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.msgkatz.ratesapp.decompose.MainIFace
import com.msgkatz.ratesapp.decompose.RootIFace
import com.msgkatz.ratesapp.decompose.SplashIFace


@Composable
fun RootContent(component: RootIFace) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(fade() + scale()),
    ) {
        when (val child = it.instance) {
            is RootIFace.Child.Main -> MainContent(child.component)
            is RootIFace.Child.Splash -> SplashContent(child.component)
        }
    }
}


@Composable
fun SplashContent(component: SplashIFace) {

}

@Composable
fun MainContent(component: MainIFace) {

}