pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Celestia"

include(":app")
include(":core")
include(":core:network")
include(":core:data")
include(":core:model")
include(":core:domain")
include(":core:ui")
include(":core:designsystem")
include(":core:common")
include(":core:testing")

include(":feature")
include(":feature:home")
include(":feature:detail")
include(":feature:location")
