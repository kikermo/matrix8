package org.kikermo.matrix8.io

import it.tangodev.ble.BleApplication
import it.tangodev.ble.BleApplicationListener
import it.tangodev.ble.BleCharacteristic
import it.tangodev.ble.BleCharacteristicListener
import it.tangodev.ble.BleService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.bluez.GattCharacteristic1
import org.kikermo.matrix8.io.BLEConnectionsState.DEVICE_CONNECTED
import org.kikermo.matrix8.io.BLEConnectionsState.DEVICE_DISCONNECTED

class BLESampleService(
    bleConnectionState: MutableSharedFlow<BLEConnectionsState>,
    private val sampleCharUpstreamPublisher: MutableStateFlow<String>,
    private val sampleCharDownstreamPublisher: StateFlow<String>,
) {
    companion object {
        private const val UUID = "59b755c8-cebd-40b8-8977-a979433c4b1b"
        private const val PATH = "/sampleBLE"
        private const val DEVICE_ALIAS = "SampleBLE"
    }

    private val appListener: BleApplicationListener = object : BleApplicationListener {
        override fun deviceDisconnected() {
            // Log or publish
            bleConnectionState.tryEmit(DEVICE_CONNECTED)
        }

        override fun deviceConnected() {
            // Log or publish
            bleConnectionState.tryEmit(DEVICE_DISCONNECTED)
        }
    }

    private val bleApp = BleApplication(PATH, appListener)
    private val bleService = BleService("$PATH/s", UUID, true)
    private val sampleCharacteristic = BLESampleCharacteristic(bleService)

    init {
        bleService.addCharacteristic(sampleCharacteristic)
        bleApp.addService(bleService)
        bleApp.setAdapterAlias(DEVICE_ALIAS)
    }

    suspend fun startService() {
        bleApp.start()

        CoroutineScope(Dispatchers.IO).launch {
            sampleCharDownstreamPublisher.collectLatest {
                sampleCharacteristic.sampleCharacteristicValue = it.toByteArray()
            }
        }

        sampleCharacteristic.sampleCharacteristicCallback = { sampleCharactersiticValue ->
            // Log or publish value
            sampleCharUpstreamPublisher.tryEmit(sampleCharactersiticValue.toString())
        }
    }

    fun stopService() {
        sampleCharacteristic.sampleCharacteristicCallback = null
        bleApp.stop()
    }
}

class BLESampleCharacteristic(
    bleService: BleService,
) : BleCharacteristic(bleService), GattCharacteristic1 {
    companion object {
        private const val UUID = "a620f5f2-3328-4d9b-9683-0f6b012c8f54"
        private const val PATH = "/bleSample/s/c"
    }

    var sampleCharacteristicValue = byteArrayOf()
        set(value) {
            field = value
            sendNotification()
        }

    var sampleCharacteristicCallback: ((ByteArray) -> Unit)? = null

    init {
        val flags = arrayListOf(
            CharacteristicFlag.READ,
            CharacteristicFlag.WRITE,
            CharacteristicFlag.NOTIFY
        )

        setFlags(flags)

        path = PATH
        uuid = UUID

        listener = object : BleCharacteristicListener {
            override fun getValue(): ByteArray {
                return sampleCharacteristicValue
            }

            override fun setValue(value: ByteArray) {
                sampleCharacteristicValue = value
                sampleCharacteristicCallback?.invoke(value)
            }
        }
    }
}

enum class BLEConnectionsState {
    DEVICE_CONNECTED,
    DEVICE_DISCONNECTED
}