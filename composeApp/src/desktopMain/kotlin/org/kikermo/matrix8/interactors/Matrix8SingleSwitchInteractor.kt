package org.kikermo.matrix8.interactors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Matrix8SingleSwitchInteractor @Inject constructor(
) {
    suspend fun toggleSwitch(toggle: Boolean) {
        return withContext(Dispatchers.IO) {
            //TODO RPi - i2c
//            try {
//                val peripheralManager = PeripheralManager.getInstance()
//                val i2cDevice = peripheralManager.openI2cDevice("I2C1", DEVICE_ADDRESS_119)
//                val command = getCommand(toggle)
//                command.forEach{ println("${it.toUByte().toString(2)}") }
//                i2cDevice.write(command, command.size)
//                i2cDevice.close()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }


//            DEVICE ADDRESS SCAN
//            val peripheralManager = PeripheralManager.getInstance()
//            for (address in 0x00..0xFF) {
//                try {
//                    val i2cDevice = peripheralManager.openI2cDevice("I2C1", address)
//                    val command = getCommand(toggle)
//                    i2cDevice.write(command, command.size)
//                    i2cDevice.close()
//                    println("!!!!Success with  $address ")
//                } catch (e: Exception) {
//                    //e.printStackTrace()
//                    println("Error with $address")
//                }
//
//            }
        }
    }

    private fun getCommand(enabled: Boolean): ByteArray {
        val data1 = if (enabled) DATA_1_ENABLE else DATA_1_DISABLE

        return listOf(data1, DATA_0)
            .map(Int::toByte)
            .toByteArray()
    }

    companion object {
        private const val DEVICE_ADDRESS = 0b11100000 // 1 1 1 0 a2 a1 a0 R/W
        private const val DEVICE_ADDRESS_119 = 0b1110111 // 1 1 1 0 a2 a1 a0 R/W, still need to figure out address
        private const val DATA_1_ENABLE = 0b10000000 // DATA AX3 AX2 AX1 AY2 AY1 AY0
        private const val DATA_1_DISABLE = 0b00000000 // DATA AX3 AX2 AX1 AY2 AY1 AY0
        private const val DATA_0 = 0b00000001 // X X X X X X X LDSW
    }
}