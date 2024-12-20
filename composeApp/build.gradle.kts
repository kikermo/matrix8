import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kapt)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.slf4j)

            implementation(project(":i2cCommunication"))
            implementation(project(":bleService"))
            implementation(project(":gpio"))

//            implementation(libs.pi4j.linuxfs)
//            implementation(libs.pi4j.gpiod)
//            implementation(libs.pi4j.raspberrypi)
        }

        commonMain.dependencies {
            implementation(project(":core"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.uiTooling)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(project.dependencies.enforcedPlatform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
        }
    }
}


compose.desktop {
    application {
        mainClass = "org.kikermo.matrix8.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Deb, TargetFormat.Pkg)
            packageName = "org.kikermo.matrix"
            packageVersion = "1.0.0"
        }
    }

}
