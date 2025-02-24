plugins {
    alias(libs.plugins.sevban.android.application)
    alias(libs.plugins.sevban.android.application.compose)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.google.maps)
}

android {
    namespace = "com.sevban.weatherapp"

    defaultConfig {
        applicationId = "com.sevban.weatherapp"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    secrets {
        defaultPropertiesFileName = "local.defaults.properties"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.domain)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
    implementation(projects.core.common)
    implementation(projects.feature.home)
    implementation(projects.feature.location)
    implementation(projects.feature.detail)

    implementation(libs.androidx.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose
    implementation(libs.bundles.compose)
    androidTestImplementation(libs.bundles.compose.testing)

    // Dagger-Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Testing
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.android.testing)
}