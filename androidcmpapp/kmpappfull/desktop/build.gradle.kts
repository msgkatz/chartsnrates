import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
//    alias(libs.plugins.kotlinMultiplatform)
//    alias(libs.plugins.composeMultiplatform)
//    alias(libs.plugins.composeCompiler)

    id(libs.plugins.kotlinMultiplatform.get().pluginId) //apply false
    id(libs.plugins.kotlin.compose.get().pluginId)
    id("org.jetbrains.compose") version "1.7.1" //"1.8.0-beta02"
    id(libs.plugins.kotlinx.serialization.get().pluginId)
}

kotlin {
    //jvm("desktop")
    jvm()
    
    sourceSets {
        //val desktopMain by getting
        val jvmMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.jb.androidx.lifecycle.viewmodel)
            implementation(libs.jb.androidx.lifecycle.runtime.compose)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)

            implementation(project(":androidcmpapp:data:cnr-model-kmm"))
            implementation(project(":androidcmpapp:data:cnr-network-kmm"))
            implementation(project(":androidcmpapp:data:cnr-repos-kmm"))
            implementation(project(":androidcmpapp:core:uikitkmp"))

            implementation(project(":androidcmpapp:feature:rootkmp"))

            implementation(libs.decompose.decompose)
            implementation(libs.decompose.extensionsCompose)
            implementation(libs.essenty.lifecycle)
            implementation(libs.badoo.reaktive)
            implementation(libs.badoo.reaktive.coroutines.interop)
        }
    }
}


compose.desktop {
    application {
        mainClass = "com.msgkatz.ratesapp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.msgkatz.ratesapp"
            packageVersion = "1.0.0"
        }
    }
}
