package org.kikermo.matrix8.io


actual class Matrix8I2CPeripheralFactory {

     fun create(): Matrix8I2CPeripheral = Matrix8I2CPeripheralImpl()
}
