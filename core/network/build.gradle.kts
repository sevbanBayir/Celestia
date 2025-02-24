import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.sevban.android.library)
    alias(libs.plugins.sevban.android.library.compose)
    alias(libs.plugins.sevban.android.hilt)
}

android {
    namespace = "com.sevban.network"

    defaultConfig {
        val apiKey = gradleLocalProperties(rootDir, providers).getProperty("API_KEY")
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(projects.core.common)

    // Retrofit
    implementation(libs.retrofit.gson)
    implementation(libs.retrofit)

    // OKHttp
    implementation(libs.okhttp3)

    // Testing
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.android.testing)
}