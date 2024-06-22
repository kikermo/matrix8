package org.kikermo.matrix8.io

import com.pi4j.Pi4J
import com.pi4j.io.spi.Spi
import com.pi4j.io.spi.SpiProvider
import java.nio.ByteBuffer


class SpiTest {
    companion object {
        private const val SPI_CHANNEL: Int = 0
    }

    private val pi4j = Pi4J.newAutoContext()
    private val config = Spi.newConfigBuilder(pi4j)
        .id("my-spi-device")
        .name("My SPI Device")
        .address(SPI_CHANNEL)
        .baud(Spi.DEFAULT_BAUD)
        .build()

    private val spiProvider = pi4j.provider<SpiProvider>("pigpio-spi")

    fun writeData(data: String): String {
        spiProvider.create(config).use { spi ->
            spi.open()
            spi.write(data)

            // Variable time before reading based on chip
            Thread.sleep(100)
            val buffer: ByteBuffer = spi.readByteBuffer(data.length)

            return String(buffer.array())
        }
    }

    fun onShutdown() {
        pi4j.shutdown()
    }
}
