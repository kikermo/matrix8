package org.kikermo.matrix8.io

import com.pi4j.Pi4J
import com.pi4j.context.Context
import com.pi4j.plugin.linuxfs.provider.i2c.LinuxFsI2CProvider
import com.pi4j.plugin.raspberrypi.platform.RaspberryPiPlatform

inline fun pi4jI2C(block: Context.() -> Unit): Context {
    val context = Pi4J.newContextBuilder()
        .add(RaspberryPiPlatform())
        .add(
            LinuxFsI2CProvider.newInstance(),
        )
        .build()
    context.run(block)
    context.shutdown()
    return context
}