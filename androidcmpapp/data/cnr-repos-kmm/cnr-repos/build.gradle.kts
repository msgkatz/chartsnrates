import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
//import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
//import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
//    alias(libs.plugins.multiplatform)
//    alias(libs.plugins.android.library)
//    alias(libs.plugins.kotlinx.serialization)
//    id("convention.publication")

    id(libs.plugins.kotlinMultiplatform.get().pluginId) //apply false
    id(libs.plugins.androidLibrary.get().pluginId) //apply false
    id(libs.plugins.kotlinx.serialization.get().pluginId)
    //kotlin("plugin.serialization") version "2.0.20"
}

group = "com.msgkatz.ratesapp.data.repos"
version = "1.0"

kotlin {
    jvmToolchain(11)
    androidTarget {
        publishLibraryVariants("release")
    }

    jvm()

    js {
        browser {
            webpackTask {
                mainOutputFileName = "cnr-repos.js"
            }
        }
        binaries.executable()
    }

//    wasmJs {
//        browser()
//        binaries.executable()
//    }

//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        moduleName = "composeApp"
//        browser {
//            val projectDirPath = project.projectDir.path
//            commonWebpackConfig {
//                outputFileName = "composeApp.js"
//                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
//                    static = (static ?: mutableListOf()).apply {
//                        // Serve sources to debug inside browser
//                        add(projectDirPath)
//                    }
//                }
//            }
//        }
//        binaries.executable()
//    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "cnr-repos"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":androidcmpapp:data:cnr-network-kmm"))
            implementation(project(":androidcmpapp:data:cnr-model-kmm"))

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotlinx.serialization.json)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
        }

        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
        }

    }

    //https://kotlinlang.org/docs/native-objc-interop.html#export-of-kdoc-comments-to-generated-objective-c-headers
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilations["main"].compilerOptions.options.freeCompilerArgs.add("-Xexport-kdoc")
    }

    task("testClasses")

}

android {
    namespace = "com.msgkatz.ratesapp.data.repos"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
    }
}
