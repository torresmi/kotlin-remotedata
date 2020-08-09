package dependencies

object Deps {
    val assertJ = dependency("org.assertj:assertj-core", Version.assertJ)
    val kotlin = dependency("org.jetbrains.kotlin:kotlin-stdlib", Version.kotlin)
    object Spek : Group("org.jetbrains.spek") {
        val api = withArtifact("spek-api", Version.spek)
        val engine = withArtifact("spek-junit-platform-engine", Version.spek)
    }
}

object Plugins {
    val bintray = dependency("com.jfrog.bintray.gradle:gradle-bintray-plugin", Version.bintray)
    val dokka = dependency("org.jetbrains.dokka:dokka-gradle-plugin", Version.dokka)
    val junit = dependency("org.junit.platform:junit-platform-gradle-plugin", Version.junitPlugin)
    val kotlin = dependency("org.jetbrains.kotlin:kotlin-gradle-plugin", Version.kotlin)
    val maven = dependency("com.github.dcendents:android-maven-gradle-plugin", Version.maven)
}

abstract class Group(val group: String) {
    fun withArtifact(artifact: String, version: String) = "$group:$artifact:$version"
}

private fun dependency(path: String, version: String) = "$path:$version"

object Version {
    val assertJ = "2.8.0"
    val bintray = "1.7.3"
    val dokka = "0.9.15"
    val junitPlugin = "1.0.0"
    val kotlin = "1.3.72"
    val maven = "1.4.1"
    val spek = "1.1.5"
}
