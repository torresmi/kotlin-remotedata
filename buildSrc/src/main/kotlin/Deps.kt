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

    object Spek : Group("org.spekframework.spek2") {
        val dslMetadata = withArtifact("spek-dsl-metadata", Versions.spek)
        val dslJvm = withArtifact("spek-dsl-jvm", Versions.spek)
        val junit5 = withArtifact("spek-runner-junit5", Versions.spek)
    }
}

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
    val coroutines = "1.4.0"
    val dokka = "1.4.10.2"
    val jacoco = "0.8.5"
    val kotest = "4.2.4"
    val kotlin = "1.4.10"
    val maven = "1.4.1"
    val spek = "2.0.13"
    val versions = "0.33.0"
}
