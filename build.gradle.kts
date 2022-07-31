plugins {
    alias(libs.plugins.kotlin.multiplatform) version libs.versions.kotlin apply false
    alias(libs.plugins.kotlin.js) version libs.versions.kotlin apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.maven) apply false
}

subprojects {

    apply {
        from("$rootDir/scripts/jacoco.gradle")
    }

    repositories {
        mavenCentral()
        jcenter()
    }
}

// Dokka

repositories {
    mavenCentral()
    jcenter()
}

tasks.withType<org.jetbrains.dokka.gradle.DokkaMultiModuleTask>().configureEach {
    outputDirectory.set(projectDir.resolve("dokka"))
}
