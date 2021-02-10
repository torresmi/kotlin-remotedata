buildscript {

    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.20")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.33.0")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.13.0")
    }
}

subprojects {

    apply {
        plugin("com.github.ben-manes.versions")
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
    outputDirectory.set(projectDir.resolve("docs/dokka"))
}

apply(plugin="org.jetbrains.dokka")
