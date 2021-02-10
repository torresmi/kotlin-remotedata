package dependencies

object Deps {
    object Coroutines : Group("org.jetbrains.kotlinx") {
        val core = withArtifact("kotlinx-coroutines-core", Versions.coroutines)
        val test = withArtifact("kotlinx-coroutines-test", Versions.coroutines)
    }

    object Kotest : Group("io.kotest") {
        val assertions = withArtifact("kotest-assertions-core", Versions.kotest)
        val property = withArtifact("kotest-property", Versions.kotest)
        val runner = withArtifact("kotest-runner-junit5", Versions.kotest)
    }

    object Kotlin : Group("org.jetbrains.kotlin") {
        val reflect = withArtifact("kotlin-reflect", Versions.kotlin)
    }
}

// Can't use in kts files yet https://github.com/gradle/gradle/issues/9270
object Plugins {
    val bintray = dependency("com.jfrog.bintray.gradle:gradle-bintray-plugin", Versions.bintray)
    val dokka = dependency("org.jetbrains.dokka:dokka-gradle-plugin", Versions.dokka)
    val kotlin = dependency("org.jetbrains.kotlin:kotlin-gradle-plugin", Versions.kotlin)
    val maven = dependency("com.github.dcendents:android-maven-gradle-plugin", Versions.maven)
    val versions = dependency("com.github.ben-manes:gradle-versions-plugin", Versions.versions)
}

abstract class Group(val group: String) {
    fun withArtifact(artifact: String, version: String) = "$group:$artifact:$version"
}

private fun dependency(path: String, version: String) = "$path:$version"

object Versions {
    val bintray = "1.8.5"
    val coroutines = "1.4.2"
    val dokka = "1.4.20"
    val jacoco = "0.8.6"
    val kotest = "4.4.0"
    val kotlin = "1.4.30"
    val maven = "1.4.1"
    val versions = "0.36.0"
}
