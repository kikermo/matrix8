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
            implementation(libs.pi4j.linuxfs)
//            implementation(libs.pi4j.gpiod)
            implementation(libs.pi4j.pigpio)
            implementation("org.slf4j:slf4j-api:1.7.32")
            implementation("org.slf4j:slf4j-simple:1.7.32")
            implementation("com.pi4j:pi4j-plugin-raspberrypi:2.3.0")
            implementation("com.pi4j:pi4j-plugin-pigpio:2.3.0")
        }
    }
}