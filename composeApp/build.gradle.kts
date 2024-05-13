import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    jvm("desktop")
    val native = linuxArm64("native")

    sourceSets {
        val desktopMain by getting

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.uiTooling)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)

        }
        val nativeMain by getting
            nativeMain.dependencies {
            implementation(libs.ktgp)
            implementation(kotlin("stdlib"))
        }
    }
    configure(listOf(native)) {
        val libs = "$buildDir/native/libs/usr/lib/aarch64-linux-gnu/"

        binaries.executable()
//        binaries.all {
//            linkTask.dependsOn(tasks.getByPath(":nativeLibs"))
//            linkerOpts.add("-L$libs")
//        }
    }

//    dependencies {
//        val skikoVersion = "0.7.55"
//        commonMainImplementation("org.jetbrains.skiko:skiko-awt-runtime-linux-arm64:$skikoVersion")
//        commonMainImplementation("org.jetbrains.skiko:skiko:$skikoVersion")
//        constraints {
//            commonMainImplementation("org.jetbrains.skiko:skiko:$skikoVersion") {
//                because("Test")
//            }
//            commonMainImplementation("org.jetbrains.skiko:skiko-awt-runtime-linux-arm64:$skikoVersion") {
//                because("Test")
//            }
//        }
//    }
}


compose.desktop {
    application {
        mainClass = "org.kikermo.matrix8.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Deb)
            packageName = "org.kikermo.matrix"
            packageVersion = "1.0.0"
        }
    }
}
