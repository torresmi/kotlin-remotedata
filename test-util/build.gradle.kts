import dependencies.Deps

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
                api(Deps.Coroutines.test)
                api(Deps.Kotest.assertions)
                api(Deps.Kotest.property)
                api(Deps.Kotest.runner)
            }
        }
    }
}
