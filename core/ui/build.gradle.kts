plugins {
    alias(libs.plugins.celestia.android.library)
    alias(libs.plugins.celestia.android.library.compose)
    alias(libs.plugins.celestia.android.hilt)
}

android {
    namespace = "com.sevban.ui"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)

    implementation(libs.androidx.activity.compose)

    // Coil
    implementation(libs.coil.compose)

    // Testing
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.android.testing)
    androidTestImplementation(libs.bundles.compose.testing)
}