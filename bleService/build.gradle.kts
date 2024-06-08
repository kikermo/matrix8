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
            implementation(files("libs/ble-java-0.1.jar"))
            implementation(files("libs/dbus-java-2.7.jar"))
            implementation(files("libs/unix.jar"))
            implementation(files("libs/libmatthew-java-0.8.jar"))
//            implementation(libs.dbus)
        }
    }
}