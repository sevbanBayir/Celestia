plugins {
    alias(libs.plugins.celestia.android.library)
    alias(libs.plugins.celestia.android.hilt)
}

android {
    namespace = "com.sevban.common"
}

dependencies {
    implementation(projects.core.model)

    // Location
    implementation(libs.gms.location)

    // Testing
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.android.testing)
}