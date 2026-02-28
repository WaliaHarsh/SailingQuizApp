rootProject.name = "quizApp"
include(":composeApp")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.software/kotlin/p/kotlin/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.software/kotlin/p/kotlin/dev")
    }
}
