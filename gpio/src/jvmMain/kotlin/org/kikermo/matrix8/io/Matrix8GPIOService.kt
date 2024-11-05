package org.kikermo.matrix8.io

import com.pi4j.context.Context
import com.pi4j.io.gpio.digital.DigitalState
import com.pi4j.io.gpio.digital.PullResistance
import com.pi4j.ktx.console
import com.pi4j.ktx.io.digital.digitalInput
import com.pi4j.ktx.io.digital.digitalOutput
import com.pi4j.ktx.io.digital.onHigh
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kikermo.matrix8.domain.model.Preset


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

                while (true) {
                    delay(1000L)
                }
            }
            title("<-- Matrix 8 -->", "Switching off")
        }
    }

    private suspend fun Context.setupLED(ledPin: Int) {
        digitalOutput(ledPin) {
            id(ledPin.toName())
            name(ledPin.toName())
            initial(DigitalState.LOW)
            provider("gpiod-digital-output")
        }.run {
            CoroutineScope(Dispatchers.IO).launch {
                presetFlow.collectLatest { preset ->
                    val presetLEDPin = preset.toLED()
                    val enabled = DigitalState.getState(presetLEDPin == ledPin)
                    state(enabled)
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

    companion object {
        private const val LED_PIN_RED = 27
        private const val LED_PIN_GREEN = 17
        private const val LED_PIN_BLUE = 5
        private const val LED_PIN_YELLOW = 6

        private const val SWITCH_PIN_1 = 22
        private const val SWITCH_PIN_2 = 16
        private const val SWITCH_PIN_3 = 24
        private const val SWITCH_PIN_4 = 23
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
}
