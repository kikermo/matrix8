package org.kikermo.matrix8.io

import com.pi4j.ktx.io.i2c
import com.pi4j.ktx.io.linuxFsI2CProvider
import com.pi4j.ktx.pi4j


class Matrix8I2CPeripheralImpl : Matrix8I2CPeripheral {
    @OptIn(ExperimentalStdlibApi::class)
    override suspend fun sendData(commandValue: List<Byte>) {
        pi4jI2C {
            providers().describe().print(System.out)
            i2c(1, ADG2188_DEVICE_ADDRESS) {
                id("ADG2188")
//                provider("pigpio-i2c")
               // provider("linuxfs-i2c")
                   linuxFsI2CProvider()
            }.use { adg2188 ->
                commandValue.forEach {
                    println("Data bits ${it.toHexString()}")
                    adg2188.write(it)
                }
            }
        }
    }

    companion object {
        private const val ADG2188_DEVICE_ADDRESS = 0x77 // 1 1 1 0 a2 a1 a0 R/W, r/w
    }
}