package org.kikermo.matrix8.io

import org.kikermo.matrix8.di.Inject
import org.kikermo.matrix8.repository.I2CPeripheral

class I2CPeripheralImpl @Inject constructor(): I2CPeripheral {
    override suspend fun open(deviceAddress: Int) {
        // NOOP
    }

    override suspend fun close() {
        // NOOP
    }

    override suspend fun sendData(commandValue: Pair<Byte, Byte>) {
        // NOOP
    }
}
