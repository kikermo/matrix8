package org.kikermo.matrix8.io

import io.ktgp.I2c
import io.ktgp.i2c.I2c
import io.ktgp.i2c.I2cDevice

class Matrix8I2CPeripheralImpl :Matrix8I2CPeripheral{
    private lateinit var i2c: I2c
    private lateinit var i2cDevice: I2cDevice
    override suspend fun open(deviceAddress: Int) {
        i2c = I2c(1)
        i2cDevice = i2c.device(deviceAddress.toUInt())
    }

    override suspend fun close() {
        i2c.close()
    }

    override suspend fun sendData(commandValue: Pair<Byte, Byte>) {
        i2cDevice.write(commandValue.first.toUByte())
        i2cDevice.write(commandValue.second.toUByte())
    }
}
