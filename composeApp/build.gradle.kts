import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    jvm("desktop")
//    val native = linuxArm64("native")

    sourceSets {
        val desktopMain by getting

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(project(":i2cCommunication"))
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.uiTooling)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)

        }
//        nativeMain.dependencies {
//            implementation(libs.ktgp)
//        }
//        val nativeMain by getting
//        nativeMain.dependencies {
//            implementation(libs.ktgp)
//
//
//            // Kotlinx
//            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-linuxarm64:1.7.3")
//            implementation(kotlin("stdlib"))
//        }

    }
//    configure(listOf(native)) {
//        val libs = "$buildDir/native/libs/usr/lib/aarch64-linux-gnu/"
//
//        binaries.executable()
//        binaries.all {
//            linkTask.dependsOn(tasks.getByPath(":nativeLibs"))
//            linkerOpts.add("-L$libs")
//        }
//    }
}


compose.desktop {
    application {
        mainClass = "org.kikermo.matrix8.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "org.kikermo.matrix"
            packageVersion = "1.0.0"
        }
    }
}
