pluginManagement {
    repositories {
        gradlePluginPortal()
        maven(url="https://dl.bintray.com/kotlin/dokka")
    }
}

plugins {
    id("com.pablisco.gradle.automodule") version "0.15"
}
