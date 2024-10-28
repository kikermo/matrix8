package org.kikermo.matrix8.io

import com.pi4j.ktx.io.i2c


class Matrix8I2CPeripheralImpl : Matrix8I2CPeripheral {
    @OptIn(ExperimentalStdlibApi::class)
    override suspend fun sendData(commandValue: List<List<Byte>>) {
        pi4jI2C {
            providers().describe().print(System.out)
            i2c(1, ADG2188_DEVICE_ADDRESS) {
                id("ADG2188")
                //linuxFsI2CProvider()
                provider("cmd-i2c")
            }.use { adg2188 ->
//                commandValue.forEach {
//                    println("Data bits ${it.toHexString()}")
//                    adg2188.write(it)
//                }
                commandValue.forEach {
                    println("Data bits ${it.toByteArray().toHexString()}")
                    adg2188.write(it.toByteArray())
                }
            }
        }
    }

    companion object {
        private const val ADG2188_DEVICE_ADDRESS = 0x77 // 1 1 1 0 a2 a1 a0 R/W, r/w
    }
}