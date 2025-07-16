plugins {
    alias(libs.plugins.celestia.android.library)
    alias(libs.plugins.celestia.android.hilt)
}

android {
    namespace = "com.sevban.data"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.common)
    implementation(projects.core.database)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    // Testing
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.android.testing)
}