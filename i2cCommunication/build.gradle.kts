plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {

    linuxArm64("nativeRPi")
    macosArm64("nativeMacOS")
    jvm()

    sourceSets {
        nativeMain.dependencies {
//            implementation(libs.ktgp)
//            implementation(kotlin("stdlib"))
        }
        val nativeRPiMain by getting
        nativeRPiMain.dependencies {
            implementation(libs.ktgp)
            implementation(kotlin("stdlib"))
        }
        val nativeMacOSMain by getting
        nativeMacOSMain.dependencies {

        }

    }
}