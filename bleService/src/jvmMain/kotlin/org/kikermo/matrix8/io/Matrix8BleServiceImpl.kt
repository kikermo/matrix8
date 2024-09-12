package org.kikermo.matrix8.io

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kikermo.bleserver.BLECharacteristic
import org.kikermo.bleserver.BLECharacteristic.AccessType
import org.kikermo.bleserver.BLEConnectionListener
import org.kikermo.bleserver.BLEServer
import org.kikermo.bleserver.BLEService
import org.kikermo.bleserver.bluez.BluezBLEServerConnector
import org.kikermo.matrix8.domain.model.Pedal
import java.util.UUID

internal class Matrix8BleServiceImpl(
    private val pedalStateFlow: MutableStateFlow<List<Pedal>>,
    private val initialPedals: List<Pedal>,
) : Matrix8BleService {
    companion object {
        private const val SERVICE_UUID = "740b93ce-c198-455a-9102-43edd3f59f6c"
        private const val SERVICE_NAME = "Matrix8"
        private const val DEVICE_NAME = "Matrix8"
        private const val CHARACTERISTIC_UUID = "718b1158-3736-4620-98d6-e57321d01a70"
        private const val CHARACTERISTIC_NAME = "pedals"
    }

    private val pedalsCharacteristic = BLECharacteristic(
        uuid = UUID.fromString(CHARACTERISTIC_UUID),
        name = CHARACTERISTIC_NAME,
        writeAccess = AccessType.Write { pedalsByteArray ->
            println("Bytes ${pedalsByteArray.joinToString { it.toString() }}")
            pedalStateFlow.value = pedalsByteArray.toPedalList()
        },
        readAccess = AccessType.Read,
        notifyAccess = AccessType.Notify
    )

    private val matrix8Service = BLEService(
        uuid = UUID.fromString(SERVICE_UUID),
        name = SERVICE_NAME,
        characteristics = listOf(pedalsCharacteristic)
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
            pedalStateFlow.collectLatest {
                setCharacteristicValue(it)
            }
        }
    }

    override suspend fun stopService() {
        bleServer.stop()
    }

    /**
     *  Characteristic format, byteArray
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
}