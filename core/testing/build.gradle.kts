plugins {
    alias(libs.plugins.celestia.android.library)
    alias(libs.plugins.celestia.android.library.compose)
}

android {
    namespace = "com.sevban.testing"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.common)

    implementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.android.testing)
}