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
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(project(autoModules.testUtil))

                runtimeOnly(Deps.Kotlin.reflect)
            }
        }
    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform {
        includeEngines("kotest")
    }
}
