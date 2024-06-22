package org.kikermo.matrix8.io

import com.pi4j.Pi4J
import com.pi4j.io.IOType
import com.pi4j.io.gpio.digital.DigitalOutputConfig
import com.pi4j.io.gpio.digital.DigitalOutputProvider
import com.pi4j.platform.Platform


class GPIOTest {
    companion object {
        private const val DIGITAL_OUTPUT_PIN: Int = 3
    }

    private val pi4j = Pi4J.newAutoContext()
    private val platform: Platform = pi4j.platforms().getDefault()
    private val provider: DigitalOutputProvider = platform.provider(IOType.DIGITAL_OUTPUT)
    private val config = DigitalOutputConfig.newBuilder(pi4j).address(DIGITAL_OUTPUT_PIN).build()
    private val output = provider.create(config)

    fun setLED(enabled: Boolean) {
        if (enabled) output.high()
        else output.low()
    }

    fun onShutdown() {
        pi4j.shutdown()
    }
}
