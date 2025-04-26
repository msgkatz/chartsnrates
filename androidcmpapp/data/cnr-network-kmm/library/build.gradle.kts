import org.jetbrains.kotlin.cli.jvm.main
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id(libs.plugins.kotlinMultiplatform.get().pluginId) //apply false
    id(libs.plugins.androidLibrary.get().pluginId) //apply false
    id(libs.plugins.kotlinx.serialization.get().pluginId)
}

kotlin {
    jvm()
    js {
        browser {
            webpackTask {
                mainOutputFileName = "cnr-repos.js"
            }
        }
        binaries.executable()
    }

    wasmJs {
        browser()
        binaries.executable()
    }

    androidTarget {
        publishLibraryVariants("debug", "release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()

    sourceSets {
        //val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.encoding)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        jvmMain.dependencies {
            implementation(libs.ktor.client.java)
        }

        jsMain.dependencies {
            implementation(libs.ktor.client.js)
        }

        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }

    task("testClasses")
}

android {
    namespace = "com.msgkatz.ratesapp.data.network"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    sourceSets["main"].setRoot("src/androidMain")
    sourceSets["release"].setRoot("src/androidMainRelease")
    sourceSets["debug"].setRoot("src/androidMainDebug")
    sourceSets["test"].setRoot("src/androidUnitTest")
    sourceSets["testRelease"].setRoot("src/androidUnitTestRelease")
    sourceSets["testDebug"].setRoot("src/androidUnitTestDebug")

}


