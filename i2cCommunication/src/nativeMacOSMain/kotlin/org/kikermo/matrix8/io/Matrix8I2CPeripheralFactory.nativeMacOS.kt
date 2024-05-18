package org.kikermo.matrix8.io

actual class Matrix8I2CPeripheralFactory {
    actual fun create(): Matrix8I2CPeripheral = object : Matrix8I2CPeripheral {
        override suspend fun open(deviceAddress: Int) {
            println("Opening I2C Matrix")
        }

        override suspend fun close() {
            println("Closing I2C Matrix")
        }

        override suspend fun sendData(commandValue: Pair<Byte, Byte>) {
            println("command ${commandValue.first} -> value ${commandValue.second}")
        }

    }
}