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
        includeEngines("kotest")
    }
}
