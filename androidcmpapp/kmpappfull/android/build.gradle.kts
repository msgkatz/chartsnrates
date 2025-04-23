import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
//    alias(libs.plugins.kotlinMultiplatform)
//    alias(libs.plugins.androidApplication)
//    alias(libs.plugins.composeMultiplatform)
//    alias(libs.plugins.composeCompiler)


    //id(libs.plugins.composeMultiplatform.get().pluginId)
    id(libs.plugins.kotlinMultiplatform.get().pluginId) //apply false
    id(libs.plugins.android.application.get().pluginId) //apply false
    id(libs.plugins.kotlin.compose.get().pluginId)
    id("org.jetbrains.compose") version "1.7.1" //"1.8.0-beta02"
    id(libs.plugins.kotlinx.serialization.get().pluginId)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(project(":androidcmpapp:data:cnr-model-kmm"))
            implementation(project(":androidcmpapp:data:cnr-network-kmm"))
            implementation(project(":androidcmpapp:data:cnr-repos-kmm"))
            implementation(project(":androidcmpapp:core:uikit"))

            implementation(project(":androidcmpapp:feature:rootkmp"))

            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(project.dependencies.platform(libs.androidx.compose.bom))
            implementation(libs.androidx.compose.runtime.android)

            implementation(libs.decompose.decompose)
            implementation(libs.decompose.extensionsCompose)
            implementation(libs.essenty.lifecycle)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
//            implementation(libs.androidx.lifecycle.viewmodel)
//            implementation(libs.androidx.lifecycle.runtime.ktx)
        }
    }
}

android {
    namespace = "com.msgkatz.ratesapp"
    compileSdk = 34 //libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.msgkatz.ratesapp"
        minSdk = 21 //libs.versions.android.minSdk.get().toInt()
        targetSdk = 34 //libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

