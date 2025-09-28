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

            implementation(libs.ktor.client.okhttp)
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

            // Ktor Client
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
        }
    }
}

android {
    val globalConfiguration = rootProject.extra //.extensions.getByName("ext")
    namespace = "com.msgkatz.ratesapp"
    compileSdk = 36 //libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.msgkatz.ratesapp"
        minSdk = 21 //libs.versions.android.minSdk.get().toInt()
        targetSdk = 36 //libs.versions.android.targetSdk.get().toInt()
        versionCode = 25
        versionName = "2.0.2"

        multiDexEnabled = true
        manifestPlaceholders["useCrashlytics"] = "false"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    signingConfigs {
        create("config_debug") {
            //keyAlias = globalConfiguration["CR_DEBUG_KEY_ALIAS"] as String
            //keyPassword = globalConfiguration["CR_DEBUG_KEY_PASSWORD"] as String
            //storeFile = file(globalConfiguration["CR_RELEASE_KEY_PATH"] as String)
            //storePassword = globalConfiguration["CR_STORE_PASSWORD"] as String
        }

        create("config_release") {
            keyAlias = globalConfiguration["CR_RELEASE_KEY_ALIAS"] as String
            keyPassword = globalConfiguration["CR_RELEASE_KEY_PASSWORD"] as String
            storeFile = file(globalConfiguration["CR_RELEASE_KEY_PATH"] as String)
            storePassword = globalConfiguration["CR_STORE_PASSWORD"] as String
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            multiDexKeepFile = file("multidex-config.txt")
            multiDexKeepProguard = file("multidex-config.pro")
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("config_release")
            manifestPlaceholders["useCrashlytics"] = "true"
        }
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            //signingConfig = signingConfigs.getByName("config_debug")
//            firebaseCrashlytics {
//                // If you don't need crash reporting for your debug build,
//                // you can speed up your build by disabling mapping file uploading.
//                mappingFileUploadEnabled = false
//            }
            signingConfig = signingConfigs.getByName("config_debug")
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

