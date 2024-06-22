package org.kikermo.matrix8.io

import com.pi4j.Pi4J
import com.pi4j.io.i2c.I2C
import com.pi4j.io.i2c.I2CProvider


class I2CTest {
    companion object {
        private const val I2C_BUS = 1
        private const val I2C_DEVICE_ADDRESS = 0x10
        private const val I2C_REGISTER_ADDRESS = 0x01
        private const val I2C_WORD_SIZE = 4
    }

    private val pi4j = Pi4J.newAutoContext()
    private val config = I2C.newConfigBuilder(pi4j)
        .id("my-i2c-bus")
        .name("My I2C Bus")
        .bus(I2C_BUS)
        .device(I2C_DEVICE_ADDRESS)
        .build()
    private val i2CProvider = pi4j.provider<I2CProvider>("pigpio-i2c");
    private val i2c = i2CProvider.create(config)

    fun writeData(data: String): String {
        val register = i2c.register(I2C_REGISTER_ADDRESS)
        register.write(data.toByteArray())
        return register.readString(I2C_WORD_SIZE)
    }

    fun onShutdown() {
        pi4j.shutdown()
    }
}
