plugins {
    alias(libs.plugins.sevban.android.library)
    alias(libs.plugins.sevban.android.hilt)
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