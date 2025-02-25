plugins {
    alias(libs.plugins.celestia.android.library)
    alias(libs.plugins.celestia.android.library.compose)
}

android {
    namespace = "com.sevban.designsystem"
}

dependencies {
    androidTestImplementation(libs.bundles.compose.testing)
}