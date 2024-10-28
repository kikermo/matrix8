plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            api(project(":core"))
            implementation(libs.coroutines)
        }
        jvmMain.dependencies {
            implementation(libs.pi4j.core)
            implementation(libs.pi4j.raspberrypi)
            implementation(libs.pi4j.ktx)

            // I/O Provider
            api(libs.pi4j.linuxfs)
            api(libs.pi4j.raspberrypi)
        }
    }
}