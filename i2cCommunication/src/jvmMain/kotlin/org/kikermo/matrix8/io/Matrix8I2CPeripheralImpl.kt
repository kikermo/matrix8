package org.kikermo.matrix8.io

import com.pi4j.io.i2c.I2C
import com.pi4j.ktx.io.i2c
import com.pi4j.ktx.io.linuxFsI2CProvider
import com.pi4j.ktx.pi4j


class Matrix8I2CPeripheralImpl : Matrix8I2CPeripheral {
    private lateinit var adg2188: I2C

    override suspend fun open() {
        pi4j {
            adg2188 = i2c(1, ADG2188_DEVICE_ADDRESS) {
                id("ADG2188")
                linuxFsI2CProvider()
            }
        }
    }

    override suspend fun close() {
        adg2188.close()
    }

    @OptIn(ExperimentalStdlibApi::class)
    override suspend fun sendData(commandValue: List<Pair<Byte, Byte>>) {
        pi4j {
            commandValue.forEach {
                println("Data bits ${it.first.toHexString()} - ${it.second.toHexString()}")
                adg2188.write(it.first, it.second)
            }
        }
    }

    companion object {
        private const val ADG2188_DEVICE_ADDRESS = 0x77 // 1 1 1 0 a2 a1 a0 R/W, r/w required?
        private const val CONTINUE_COMMAND = 0x00
        private const val FINISH_COMMAND = 0x01
    }
}