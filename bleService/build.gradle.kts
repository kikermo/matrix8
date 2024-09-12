plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
            implementation(libs.coroutines)
        }
        jvmMain.dependencies {
            implementation(libs.ble.server.core)
            implementation(libs.ble.server.bluez)
        }
    }
}