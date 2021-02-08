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
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(project(autoModules.testUtil))

                runtimeOnly(Deps.Kotlin.reflect)
            }
        }
    }

    targets {
        iosArm64()
        watchosArm64()
        tvosArm64()
        js()
        macosX64()
        linuxX64()
        mingwX64()
    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform {
        includeEngines("kotest")
    }
}
