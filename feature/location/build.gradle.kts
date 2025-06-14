import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.celestia.android.library)
    alias(libs.plugins.celestia.android.library.compose)
    alias(libs.plugins.celestia.android.hilt)
    alias(libs.plugins.celestia.android.feature)

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
    // https://issuetracker.google.com/issues/412867387#comment3 places sdk needs material dep.
    implementation(libs.material)


    // Testing
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.android.testing)
}