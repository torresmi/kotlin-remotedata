import dependencies.Deps

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
    id("com.github.dcendents.android-maven")
    id("com.jfrog.bintray")
}

kotlin {
    jvm()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(autoModules.remotedata))
                implementation(Deps.Coroutines.core)
                implementation(Deps.Coroutines.test)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(Deps.Kotest.assertions)
                implementation(Deps.Spek.dslMetadata)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(project(autoModules.testUtil))
                implementation(Deps.Spek.dslJvm)

                runtimeOnly(Deps.Kotlin.reflect)
                runtimeOnly(Deps.Spek.junit5)
            }
        }
    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform {
        includeEngines("spek2", "kotest")
    }
}
