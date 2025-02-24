plugins {
    alias(libs.plugins.sevban.android.library)
    alias(libs.plugins.sevban.android.library.compose)
}

android {
    namespace = "com.sevban.designsystem"
}

dependencies {
    androidTestImplementation(libs.bundles.compose.testing)
}