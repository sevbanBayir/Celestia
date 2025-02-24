plugins {
    alias(libs.plugins.sevban.android.library)
    alias(libs.plugins.sevban.android.library.compose)
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