import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.sevban.android.library)
    alias(libs.plugins.sevban.android.library.compose)
    alias(libs.plugins.sevban.android.hilt)
    alias(libs.plugins.sevban.android.feature)

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.junit5.engine)
}

val localDefaults = Properties()
val localDefaultsFile = rootProject.file("local.defaults.properties")
if (localDefaultsFile.exists()) {
    localDefaults.load(FileInputStream(localDefaultsFile))
}

android {
    namespace = "com.sevban.location"

    defaultConfig {
        buildConfigField("String", "MAPS_API_KEY", "\"${localDefaults["MAPS_API_KEY"]}\"")
    }
}

dependencies {
    // Compose
    implementation(libs.bundles.compose)
    androidTestImplementation(libs.bundles.compose.testing)
    // Maps
    implementation(libs.google.maps.compose)
    implementation(libs.google.maps.compose.utils)
    implementation(libs.google.maps.places)

    // Coil
    implementation(libs.coil.compose)

    // Serialization
    implementation(libs.kotlin.serialization)


    // Testing
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.android.testing)
}