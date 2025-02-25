plugins {
    alias(libs.plugins.sevban.android.library)
}

android {
    namespace = "com.sevban.androidtest"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.common)

    implementation(libs.hilt.android.testing)
    implementation(libs.junit4)

    implementation(libs.bundles.testing)
    implementation(libs.bundles.android.testing)
}


