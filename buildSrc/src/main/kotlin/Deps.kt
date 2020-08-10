package dependencies

object Deps {
    val assertJ = dependency("org.assertj:assertj-core", Version.assertJ)
    object Kotest : Group("io.kotest") {
        val assertions = withArtifact("kotest-assertions-core-jvm", Version.kotest)
        val console = withArtifact("kotest-runner-console-jvm", Version.kotest)
        val property = withArtifact("kotest-property-jvm", Version.kotest)
        val runner = withArtifact("kotest-runner-junit5-jvm", Version.kotest)
    }
    val kotlin = dependency("org.jetbrains.kotlin:kotlin-stdlib", Version.kotlin)
}

object Plugins {
    val bintray = dependency("com.jfrog.bintray.gradle:gradle-bintray-plugin", Version.bintray)
    val dokka = dependency("org.jetbrains.dokka:dokka-gradle-plugin", Version.dokka)
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
    val kotest = "4.1.3"
    val kotestConsole = "4.1.3"
    val kotlin = "1.3.72"
    val maven = "1.4.1"
}
