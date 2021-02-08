import dependencies.Deps

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
    id("com.github.dcendents.android-maven")
    id("com.jfrog.bintray")
}

kotlin {
    jvm()

    js {
        browser()
        nodejs()
    }

    ios()
    tvos()
    watchos()

    linuxX64()
    macosX64()
    mingwX64()

    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation(Deps.Kotest.assertions)

                implementation(project(autoModules.testUtil))

                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmTest by getting {
            dependencies {

                implementation(kotlin("test-junit"))

                runtimeOnly(Deps.Kotlin.reflect)
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform {
        includeEngines("kotest")
    }
}
