plugins {
    //id("root.publication")
    //trick: for the same plugin versions in all sub-modules

    //alias(libs.plugins.androidLibrary).apply(false)
    //alias(libs.plugins.kotlinMultiplatform).apply(false)
    id(libs.plugins.kotlinMultiplatform.get().pluginId) apply false
    id(libs.plugins.androidLibrary.get().pluginId) apply false
}
