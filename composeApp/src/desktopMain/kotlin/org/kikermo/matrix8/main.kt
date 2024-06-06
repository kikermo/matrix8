package org.kikermo.matrix8

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.kikermo.matrix8.di.matrix8Module
import org.koin.core.context.startKoin

fun main() = application {

    initApp()

//    val state = WindowState(placement = WindowPlacement.Fullscreen)
    val state = WindowState(placement = WindowPlacement.Maximized)
    Window(onCloseRequest = ::exitApplication, state = state) {
        App()
    }
}


private fun initApp() {
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "INFO")
    startKoin {
        modules(matrix8Module)
    }
}