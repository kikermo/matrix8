package org.kikermo.matrix8.io

import com.pi4j.context.Context
import com.pi4j.io.gpio.digital.DigitalState
import com.pi4j.io.gpio.digital.PullResistance
import com.pi4j.ktx.console
import com.pi4j.ktx.io.digital.digitalInput
import com.pi4j.ktx.io.digital.digitalOutput
import com.pi4j.ktx.io.digital.onHigh
import com.pi4j.ktx.io.i2c
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.kikermo.matrix8.domain.model.Pedal
import org.kikermo.matrix8.domain.model.Preset
import java.lang.Thread.sleep


actual class Matrix8GPIOService(
    private val presetFlow: MutableStateFlow<Preset>,
    private val initialPresets: List<Preset>,
) {
    actual suspend fun start() {

        console {
            title("<-- Matrix 8 -->", "Setting up Switches and LEDs")
            pi4jGPIO {
                println("All providers")
                providers().describe().print(System.out)

                setupLED(LED_PIN_RED)
                setupLED(LED_PIN_GREEN)
                setupLED(LED_PIN_YELLOW)
                setupLED(LED_PIN_BLUE)

                setupSwitch(SWITCH_PIN_1)
                setupSwitch(SWITCH_PIN_2)
                setupSwitch(SWITCH_PIN_3)
                setupSwitch(SWITCH_PIN_4)

//                setupI2C()

                while (true) {
                    sleep(2000L)
                }

            }
        }
    }

    private fun Context.setupLED(ledPin: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            digitalOutput(ledPin) {
                id(ledPin.toName())
                name(ledPin.toName())
                initial(DigitalState.LOW)
                provider("gpiod-digital-output")
            }.run {
                while (true) {
                    val presetLEDPin = presetFlow.value.toLED()
                    val enabled = DigitalState.getState(presetLEDPin == ledPin)
                    state(enabled)
                    sleep(500L)
                }
            }
        }
    }

    private fun Preset.toLED(): Int {
        return when (id) {
            "A" -> LED_PIN_BLUE
            "B" -> LED_PIN_YELLOW
            "C" -> LED_PIN_RED
            "D" -> LED_PIN_GREEN
            else -> LED_PIN_BLUE
        }
    }

    private fun Context.setupSwitch(switchPin: Int) {
        digitalInput(switchPin) {
            id("switch$switchPin")
            name("Switch $switchPin")
            pull(PullResistance.PULL_DOWN)
            debounce(3000L)
            provider("gpiod-digital-input")
        }.run {
            onHigh {
                println("On High Pin $switchPin")
                presetFlow.value = switchPin.switchPinToPreset()
            }
        }
    }

    private fun Int.switchPinToPreset(): Preset {
        return when (this) {
            SWITCH_PIN_1 -> initialPresets.getOrNull(0)
            SWITCH_PIN_2 -> initialPresets.getOrNull(1)
            SWITCH_PIN_3 -> initialPresets.getOrNull(2)
            SWITCH_PIN_4 -> initialPresets.getOrNull(3)
            else -> initialPresets.first()
        } ?: initialPresets.first()
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun Context.setupI2C() {

        i2c(1, ADG2188_DEVICE_ADDRESS) {
            id("ADG2188")
            provider("linuxfs-i2c")
        }.use { adg2188 ->
            while (true) {
                val commands = getCommandValueList(presetFlow.value.pedals)
                adg2188.write(0x80)
                adg2188.write(0x01)
                if (commands.isNotEmpty()) {

//                    commands.forEach {
//                        println("Data bits ${it.toHexString()}")
//                        adg2188.write(it)
//                    }
                }
                sleep(500L)
            }
        }
    }


    companion object {
        private const val LED_PIN_RED = 27
        private const val LED_PIN_GREEN = 17
        private const val LED_PIN_BLUE = 5
        private const val LED_PIN_YELLOW = 6

        private const val SWITCH_PIN_1 = 22
        private const val SWITCH_PIN_2 = 16
        private const val SWITCH_PIN_3 = 24
        private const val SWITCH_PIN_4 = 23

        private const val ADG2188_DEVICE_ADDRESS = 0x77 // 1 1 1 0 a2 a1 a0 R/W, r/w
        private const val DATA_0_END = 0b00000001 // X X X X X X X LDSW
        private const val DATA_0_CONTINUE = 0b00000000 // X X X X X X X LDSW
    }

    private fun Int.toName(): String {
        return when (this) {
            LED_PIN_RED -> "RED_LED"
            LED_PIN_GREEN -> "GREEN_LED"
            LED_PIN_BLUE -> "BLUE_LED"
            LED_PIN_YELLOW -> "YELLOW_LED"
            else -> "GENERIC_LED"
        }
    }

    private fun getCommandValueList(pedals: List<Pedal>): List<Byte> {
//        val oldMatrix = matrixPersister.getPreviousMatrix()
        val oldMatrix = getMatrix(pedals)
        val newMatrix = getMatrix(pedals)
        if (oldMatrix.contentEquals(newMatrix)) {
            return listOf()
        }
      //  matrixPersister.updateMatrix(newMatrix)

        val addressByteList = getAddressByteList(oldMatrix, newMatrix)

        return addressByteList
            .map(AddressByte::toByte)
            .mapIndexed { index, byte ->
                if (addressByteList.lastIndex == index) {  // Last element?
                    listOf(byte, DATA_0_END.toByte())
                } else {
                    listOf(byte, DATA_0_CONTINUE.toByte())
                }
            }.flatten()
    }

    private fun getAddressByteList(
        oldMatrix: Array<BooleanArray>,
        newMatrix: Array<BooleanArray>
    ): List<AddressByte> {
        val addressesPairList = mutableListOf<AddressByte>()

        for (x in oldMatrix.indices) {
            for (y in oldMatrix[x].indices) {
                if (oldMatrix[x][y] != newMatrix[x][y]) {
                    addressesPairList.add(
                        AddressByte(
                            enabled = newMatrix[x][y],
                            x = x,
                            y = y,
                        )
                    )
                }
            }
        }

        return addressesPairList.toList()
    }

    private fun getMatrix(pedals: List<Pedal>): Array<BooleanArray> {
        val enabledPedals = pedals.filter { it.enabled }
        val matrix = Array(8) { BooleanArray(8) }
        if (enabledPedals.isEmpty()) {
            matrix[0][0] = true
            return matrix
        }
        enabledPedals.forEachIndexed { index, pedal ->
            when (index) {
                0 -> {
                    matrix[0][pedal.ioChannel] = true
                    // If only 1 pedal in the list, output index should be 0 (output)
                    val outputIndex = enabledPedals.getOrNull(1)?.ioChannel ?: 0
                    matrix[pedal.ioChannel][outputIndex] = true
                }

                enabledPedals.lastIndex -> matrix[pedal.ioChannel][0] = true
                else -> matrix[pedal.ioChannel][enabledPedals[index + 1].ioChannel] = true
            }
        }
        return matrix
    }

    private data class AddressByte(
        val enabled: Boolean,
        val x: Int,
        val y: Int,
    ) {
        fun toByte(): Byte {
            val enableBit = if (enabled) 1 else 0
            return (((enableBit shl 7) or (x.xAddressOffset() shl 3)) or y).toByte()
        }

        private fun Int.xAddressOffset(): Int {
            return if (this < 6) return this else (this + 2)  // 0bX0110000 and 0bX0111000 are reserved
        }
    }
}
