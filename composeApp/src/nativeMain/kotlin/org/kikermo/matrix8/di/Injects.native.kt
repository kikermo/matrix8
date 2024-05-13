package org.kikermo.matrix8.di

import org.kikermo.matrix8.io.I2CPeripheralImpl
import org.kikermo.matrix8.repository.I2CPeripheral

actual fun i2CPeripheralBuilder(): I2CPeripheral {
    return I2CPeripheralImpl()
}