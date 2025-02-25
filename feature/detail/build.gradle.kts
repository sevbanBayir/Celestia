plugins {
    alias(libs.plugins.celestia.android.library)
    alias(libs.plugins.celestia.android.library.compose)
    alias(libs.plugins.celestia.android.hilt)
    alias(libs.plugins.celestia.android.feature)

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.junit5.engine)
}

android {
    namespace = "com.sevban.detail"
}

dependencies {
    // Coil
    implementation(libs.coil.compose)

    // Serialization
    implementation(libs.kotlin.serialization)

    // Testing
    testImplementation(libs.bundles.testing)
    testImplementation(projects.core.testing)
    androidTestImplementation(libs.bundles.android.testing)
    androidTestImplementation(libs.bundles.compose.testing)
}