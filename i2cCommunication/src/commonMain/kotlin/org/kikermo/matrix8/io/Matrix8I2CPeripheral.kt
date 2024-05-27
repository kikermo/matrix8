package org.kikermo.matrix8.io

interface Matrix8I2CPeripheral {

    suspend fun open()

    suspend fun close()

    suspend fun sendData(commandValue: List<Pair<Byte, Byte>>)
}
