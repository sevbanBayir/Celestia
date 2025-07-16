plugins {
    alias(libs.plugins.celestia.android.library)
    alias(libs.plugins.celestia.android.hilt)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.sevban.sync"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.data)
    implementation(projects.core.database)
    implementation(projects.core.network)
    
    // WorkManager
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.work.hilt)
    ksp(libs.hilt.android.compiler)
    
    // Coroutines
    implementation(libs.androidx.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    
    // Location
    implementation(libs.gms.location)
    
    // Testing
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.android.testing)
} 