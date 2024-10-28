package org.kikermo.matrix8.io

interface Matrix8I2CPeripheral {
    suspend fun sendData(commandValue: List<List<Byte>>)
}
