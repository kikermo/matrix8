package org.kikermo.matrix8

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.kikermo.matrix8.di.matrix8Module
import org.kikermo.matrix8.io.Matrix8BleService
import org.kikermo.matrix8.io.Matrix8GPIOService
import org.kikermo.matrix8.io.Matrix8I2CService
import org.koin.core.context.startKoin
import java.lang.Thread.sleep

const val runCompose = true

fun main() {

    if (runCompose) {
        application {
            initApp()

            val state = WindowState(placement = WindowPlacement.Fullscreen)

            Window(onCloseRequest = ::exitApplication, state = state) {
                App()
            }
        }
    } else {
        initApp()
        println("Starting")

        while (true) {
            sleep(2000L)
        }
    }
}


private fun initApp() {
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "INFO")
    startKoin {
        modules(matrix8Module)
        val gpioService: Matrix8GPIOService = koin.get()
        val bleService: Matrix8BleService = koin.get()
      //  val i2cServer: Matrix8I2CService = koin.get()
        CoroutineScope(Dispatchers.IO).launch {
           // val i2cStart = async { i2cServer.start() }
            val gpioStart = async { gpioService.start() }
            val bleStart = async { bleService.startService() }
         //   i2cStart.await()
            gpioStart.await()
            bleStart.await()
//            gpioService.start()
  //          bleService.stopService()
        }
    }
}