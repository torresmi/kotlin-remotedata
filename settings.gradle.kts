pluginManagement {
    repositories {
        gradlePluginPortal()
        maven(url="https://dl.bintray.com/kotlin/dokka")
    }
}

plugins {
    id("com.pablisco.gradle.auto.include") version "1.3"
}
