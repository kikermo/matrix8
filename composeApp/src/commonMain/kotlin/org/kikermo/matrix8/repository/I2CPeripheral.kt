package org.kikermo.matrix8.repository

interface I2CPeripheral {
    suspend fun open(deviceAddress: Int)
    suspend fun close()
    suspend fun sendData(commandValue: Pair<Byte, Byte>)
}
