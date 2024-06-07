package org.kikermo.matrix8.io

import it.tangodev.ble.BleApplication
import it.tangodev.ble.BleApplicationListener
import it.tangodev.ble.BleService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kikermo.matrix8.domain.model.Pedal

internal class Matrix8BleServiceImpl(
    private val pedalStateFlow: MutableStateFlow<List<Pedal>>,
    private val initialPedals: List<Pedal>,
) : Matrix8BleService {
    companion object {
        private const val UUID = "740b93ce-c198-455a-9102-43edd3f59f6c"
        private const val PATH = "/matrix8"
        private const val DEVICE_ALIAS = "Matrix8"
        private const val DEVICE_NAME = "Matrix8"
    }

    private val appListener: BleApplicationListener = object : BleApplicationListener {
        override fun deviceDisconnected() {
            println("Device disconnected")
        }

        override fun deviceConnected() {
            println("Device connected")
        }
    }

    private val bleApp = BleApplication(PATH, appListener, DEVICE_NAME)
    private val bleService = BleService("$PATH/s", UUID, true)
    private val matrix8Characteristic = Matrix8Characteristic(bleService)

    init {
        bleService.addCharacteristic(matrix8Characteristic)
        bleApp.addService(bleService)
        bleApp.setAdapterAlias(DEVICE_ALIAS)
    }

    override suspend fun startService() {
        bleApp.start()

        CoroutineScope(Dispatchers.IO).launch {
            pedalStateFlow.collectLatest {
                setCharacteristicValue(it)
            }
        }

        matrix8Characteristic.matrix8Callback = {
            pedalStateFlow.value = it.toPedalList()
        }
    }

    override suspend fun stopService() {
        matrix8Characteristic.matrix8Callback = null
        bleApp.stop()
    }

    private fun setCharacteristicValue(pedals: List<Pedal>) {
        matrix8Characteristic.matrix8State = pedals.map { it.ioChannel.toByte() }.toByteArray()
    }

    private fun ByteArray.toPedalList(): List<Pedal> {
        return map { ioChannel ->
            initialPedals.find { it.ioChannel == ioChannel.toInt() }
        }.mapNotNull { it }
    }
}