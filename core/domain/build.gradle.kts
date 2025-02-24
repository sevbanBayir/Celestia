plugins {
    alias(libs.plugins.sevban.android.library)
    alias(libs.plugins.sevban.android.library.compose)
    alias(libs.plugins.sevban.android.hilt)
}

android {
    namespace = "com.sevban.domain"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.data)
    implementation(projects.core.common)
}