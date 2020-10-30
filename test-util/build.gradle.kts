import dependencies.Deps

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(autoModules.remotedata))

    api(Deps.Coroutines.test)
    api(Deps.Kotest.assertions)
    api(Deps.Kotest.property)
    api(Deps.Kotest.runner)
}
