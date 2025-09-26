import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    id(libs.plugins.kotlinMultiplatform.get().pluginId) //apply false
    id(libs.plugins.kotlin.compose.get().pluginId)
    id("org.jetbrains.compose") version "1.7.1" //"1.8.0-beta02"
    id(libs.plugins.kotlinx.serialization.get().pluginId)
}

kotlin {

    js {
        browser {
            webpackTask {
                mainOutputFileName = "cnrApp.js"
            }
        }
        binaries.executable()
    }

//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        browser()
//        binaries.executable()
//    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "cnrApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "cnrApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
//            implementation(libs.androidx.lifecycle.viewmodel)
//            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.jb.androidx.lifecycle.viewmodel)
            implementation(libs.jb.androidx.lifecycle.runtime.compose)

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
            implementation(libs.kotlinx.serialization.json)

            // Ktor Client
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
        }

        jsMain.dependencies {
            implementation(libs.ktor.client.js)
        }

        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
            //implementation(libs.ktor.client.wasm)
        }
    }
}

compose.experimental {
    web.application {
    }
}

