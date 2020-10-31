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
        }

        val commonTest by getting {
            dependencies {
                implementation(Deps.Kotest.assertions)
                implementation(Deps.Spek.dslMetadata)
                implementation(Deps.Spek.dslLinux)
                implementation(Deps.Spek.dslMacos)
                implementation(Deps.Spek.dslWindows)
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

    targets {
        iosArm64()
        watchosArm64()
        tvosArm64()
        macosX64()
        linuxX64()
        mingwX64()
    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform {
        includeEngines("spek2", "kotest")
    }
}
