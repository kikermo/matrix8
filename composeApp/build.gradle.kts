import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)

    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    jvm("desktop")

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
