pluginManagement {
    repositories {
        gradlePluginPortal()
        maven(url="https://dl.bintray.com/kotlin/dokka")
    }
}

include(
    ":remotedata",
    ":remotedata-coroutines",
    ":test-util",
)
