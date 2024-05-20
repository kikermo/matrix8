package org.kikermo.matrix8

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "INFO");

    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    App()
}