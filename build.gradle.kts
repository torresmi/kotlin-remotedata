buildscript {

    repositories {
        jcenter()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }

    dependencies {
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.10.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
        classpath("com.github.dcendents:android-maven-gradle-plugin:1.4.1")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.33.0")
    }
}

subprojects {

    apply {
        plugin("com.github.ben-manes.versions")
        from("$rootDir/scripts/jacoco.gradle")
    }

    buildscript {
        repositories {
            jcenter()
        }
    }

    repositories {
        jcenter()
        maven { url = uri("http://dl.bintray.com/jetbrains/spek") }
    }
}
