package org.kikermo.matrix8.io

import it.tangodev.ble.BleCharacteristic
import it.tangodev.ble.BleCharacteristicListener
import it.tangodev.ble.BleService
import org.bluez.GattCharacteristic1

class Matrix8Characteristic(
    bleService: BleService
) : BleCharacteristic(bleService), GattCharacteristic1 {

    companion object {
        private const val UUID = "718b1158-3736-4620-98d6-e57321d01a70"
        private const val PATH = "/matrix8/s/c"
    }

    var matrix8State = byteArrayOf()
        set(value) {
            field = value
            sendNotification()
        }

    var matrix8Callback: ((ByteArray) -> Unit)? = null

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
                return matrix8State
            }

            override fun setValue(value: ByteArray) {
                matrix8State = value
                matrix8Callback?.invoke(value)
            }
        }
    }
}
