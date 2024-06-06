package org.kikermo.matrix8.io

import it.tangodev.ble.BleApplication
import it.tangodev.ble.BleApplicationListener
import it.tangodev.ble.BleService

internal class Matrix8BleServiceImpl : Matrix8BleService {
    companion object {
        private const val UUID = "740b93ce-c198-455a-9102-43edd3f59f6c"
        private const val PATH = "/matrix8"
        private const val DEVICE_ALIAS = "Matrix8"
    }

    private val appListener: BleApplicationListener = object : BleApplicationListener {
        override fun deviceDisconnected() {
            println("Device disconnected")
        }

        override fun deviceConnected() {
            println("Device connected")
        }
    }

    private val bleApp = BleApplication(PATH, appListener)
    private val bleService = BleService("$PATH/s", UUID, true)
    private val matrix8Characteristic = Matrix8Characteristic(bleService)

    init {
        bleService.addCharacteristic(matrix8Characteristic)
        bleApp.addService(bleService)
        bleApp.setAdapterAlias(DEVICE_ALIAS)
    }

    override suspend fun startService() {
        bleApp.start()
    }

    override suspend fun stopService() {
        matrix8Characteristic.matrix8Callback = null
        bleApp.stop()
    }

    override suspend fun setCallback(callback: (ByteArray) -> Unit) {
        matrix8Characteristic.matrix8Callback = callback
    }
}