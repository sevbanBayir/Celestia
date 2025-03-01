plugins {
    alias(libs.plugins.celestia.android.library)
    alias(libs.plugins.celestia.android.library.compose)
    alias(libs.plugins.celestia.android.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.sevban.ui"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)

    // Serialization
    implementation(libs.kotlin.serialization)

    implementation(libs.androidx.navigation.compose)

    // Coil
    implementation(libs.coil.compose)

    // Testing
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.android.testing)
    androidTestImplementation(libs.bundles.compose.testing)
}