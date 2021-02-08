buildscript {

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.10.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.33.0")
    }
}

subprojects {

    apply {
        plugin("com.github.ben-manes.versions")
        from("$rootDir/scripts/jacoco.gradle")
    }

    repositories {
        mavenCentral()
    }
}

// Dokka

repositories {
    mavenCentral()
}

apply(plugin="org.jetbrains.dokka")
