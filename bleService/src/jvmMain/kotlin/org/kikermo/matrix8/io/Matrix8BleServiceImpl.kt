package org.kikermo.matrix8.io

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.kikermo.bleserver.BLECharacteristic
import org.kikermo.bleserver.BLECharacteristic.AccessType
import org.kikermo.bleserver.BLEConnectionListener
import org.kikermo.bleserver.BLEServer
import org.kikermo.bleserver.BLEService
import org.kikermo.bleserver.bluez.BluezBLEServerConnector
import org.kikermo.matrix8.domain.model.Pedal
import org.kikermo.matrix8.domain.model.Preset
import java.util.UUID

internal class Matrix8BleServiceImpl(
    private val presetStateFlow: MutableStateFlow<Preset>,
    private val initialPedals: List<Pedal>,
    private val initialPresets: List<Preset>
) : Matrix8BleService {
    companion object {
        private const val SERVICE_UUID = "740b93ce-c198-455a-9102-43edd3f59f6c"
        private const val SERVICE_NAME = "Matrix8"
        private const val DEVICE_NAME = "Matrix8"
        private const val CHARACTERISTIC_UUID_PEDALS = "718b1158-3736-4620-98d6-e57321d01a70"
        private const val CHARACTERISTIC_UUID_PRESETS = "4fedda90-5d9b-4e32-b725-d47c2429544b"
        private const val CHARACTERISTIC_NAME_PEDALS = "pedals"
        private const val CHARACTERISTIC_NAME_PRESETS = "presets"
    }

    private val pedalsCharacteristic = BLECharacteristic(
        uuid = UUID.fromString(CHARACTERISTIC_UUID_PEDALS),
        name = CHARACTERISTIC_NAME_PEDALS,
        writeAccess = AccessType.Write { pedalsByteArray ->
            println("Bytes ${pedalsByteArray.joinToString { it.toString() }}")
            presetStateFlow.value = presetStateFlow.value.copy(pedals = pedalsByteArray.toPedalList())
        },
        readAccess = AccessType.Read,
        notifyAccess = AccessType.Notify
    )

    private val presetsCharacteristic = BLECharacteristic(
        uuid = UUID.fromString(CHARACTERISTIC_UUID_PRESETS),
        name = CHARACTERISTIC_NAME_PRESETS,
        writeAccess = AccessType.Write { presetByteArray ->
            val presetIndex = presetByteArray.first().toInt() // 00-A, 01-B, 02-C, 03-D, XX-A
            presetStateFlow.value = initialPresets.getOrNull(presetIndex) ?: initialPresets.first()
        },
        readAccess = AccessType.Read,
        notifyAccess = AccessType.Notify
    )

    private val matrix8Service = BLEService(
        uuid = UUID.fromString(SERVICE_UUID),
        name = SERVICE_NAME,
        characteristics = listOf(pedalsCharacteristic, presetsCharacteristic)
    )
    private val connectionListener = object : BLEConnectionListener {
        override fun onDeviceConnected(deviceName: String, deviceAddress: String) {
            println("device connected $deviceName, $deviceAddress")
        }

        override fun onDeviceDisconnected() {
            println("device disconnected")
        }

    }
    private val bleServer = BLEServer(
        services = listOf(matrix8Service),
        serverName = DEVICE_NAME,
        connectionListener = connectionListener,
        bleServerConnector = BluezBLEServerConnector()
    )

    init {
        bleServer.primaryService = matrix8Service
    }

    override suspend fun startService() {
        bleServer.start()

        CoroutineScope(Dispatchers.IO).launch {
            presetStateFlow.map { it.pedals }.collectLatest {
                setCharacteristicValue(it)
            }

            presetStateFlow.collectLatest {
                presetsCharacteristic.value = it.toCharacteristicValue()
            }
        }
    }

    override suspend fun stopService() {
        bleServer.stop()
    }

    /**
     *  Pedals Characteristic format, byteArray
     *  ChannelNumber[0],Enabled[0], ChannelNumber[1],Enabled[1].......
     */

    private fun setCharacteristicValue(pedals: List<Pedal>) {
        pedalsCharacteristic.value = pedals.map {
            val channel = it.ioChannel.toByte()
            val enabled: Byte = if (it.enabled) 1 else 0
            listOf(channel, enabled)
        }.flatten().toByteArray()
    }

    private fun ByteArray.toPedalList(): List<Pedal> {
        if (this.size % 2 != 0)
            return emptyList()

        return this.toList().chunked(2).mapNotNull { bytePair ->
            val ioChannel = bytePair[0].toInt()
            val enabled = bytePair[1] > 0
            initialPedals.find { it.ioChannel == ioChannel }?.copy(enabled = enabled)
        }
    }

    /**
     *  Preset Characteristic format, byteArray
     *  00/XX -> A, 01 -> B, 02 -> C, 03-> D
     */
    private fun Preset.toCharacteristicValue(): ByteArray {
        return when(id) {
            "A" -> byteArrayOf(0)
            "B" -> byteArrayOf(1)
            "C" -> byteArrayOf(2)
            "D" -> byteArrayOf(3)
            else -> byteArrayOf(0)
        }
    }
}