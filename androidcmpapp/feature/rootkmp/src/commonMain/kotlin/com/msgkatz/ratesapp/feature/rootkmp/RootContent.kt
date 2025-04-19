package com.msgkatz.ratesapp.feature.rootkmp

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.msgkatz.ratesapp.feature.rootkmp.RootIFace.Child
import com.msgkatz.ratesapp.feature.rootkmp.main.MainContent
import com.msgkatz.ratesapp.feature.rootkmp.splash.SplashContent


@Composable
fun RootContent(component: RootIFace) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(fade() + scale()),
    ) {
        when (val child = it.instance) {
            is Child.Main -> MainContent(child.component)
            is Child.Splash -> SplashContent(component = child.component)
        }
    }
}