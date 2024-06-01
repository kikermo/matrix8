package org.kikermo.matrix8

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "INFO");

    val state = WindowState(placement =  WindowPlacement.Fullscreen)
    Window(onCloseRequest = ::exitApplication, state = state) {
        App()
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    App()
}