plugins {
    kotlin("multiplatform")
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
        val commonMain by getting {
            dependencies {
                implementation(project(":remotedata"))
            }
        }

        val jvmMain by getting {
            dependencies {
                api(libs.coroutines.test)
                api(libs.kotest.assertions)
                api(libs.kotest.property)
                api(libs.kotest.runner)
            }
        }
    }
}
