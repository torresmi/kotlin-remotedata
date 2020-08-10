package dependencies

object Deps {
    object Kotest : Group("io.kotest") {
        val assertions = withArtifact("kotest-assertions-core-jvm", Versions.kotest)
        val console = withArtifact("kotest-runner-console-jvm", Versions.kotest)
        val property = withArtifact("kotest-property-jvm", Versions.kotest)
        val runner = withArtifact("kotest-runner-junit5-jvm", Versions.kotest)
    }
    val kotlin = dependency("org.jetbrains.kotlin:kotlin-stdlib", Versions.kotlin)
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
    val bintray = "1.7.3"
    val dokka = "0.9.15"
    val kotest = "4.1.3"
    val kotlin = "1.3.72"
    val maven = "1.4.1"
    val versions = "0.29.0"
}
