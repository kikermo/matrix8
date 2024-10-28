package org.kikermo.matrix8.io

import com.pi4j.Pi4J
import com.pi4j.context.Context
import com.pi4j.plugin.gpiod.provider.gpio.digital.GpioDDigitalInputProvider
import com.pi4j.plugin.gpiod.provider.gpio.digital.GpioDDigitalOutputProvider
import com.pi4j.plugin.raspberrypi.platform.RaspberryPiPlatform

inline fun pi4jGPIO(block: Context.() -> Unit): Context {
    val context = Pi4J.newContextBuilder()
        .add(RaspberryPiPlatform())
        .add(
            GpioDDigitalInputProvider.newInstance(),
            GpioDDigitalOutputProvider.newInstance(),
        )
        .build()
    context.run(block)
    context.shutdown()
    return context
}