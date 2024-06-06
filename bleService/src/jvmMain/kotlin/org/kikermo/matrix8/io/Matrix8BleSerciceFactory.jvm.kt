package org.kikermo.matrix8.io

actual class Matrix8BleServiceFactory {
    actual fun create(): Matrix8BleService = Matrix8BleServiceImpl()
}