package org.kikermo.matrix8.io

import com.pi4j.io.gpio.digital.DigitalState
import com.pi4j.io.gpio.digital.PullResistance
import com.pi4j.ktx.console
import com.pi4j.ktx.io.digital.digitalInput
import com.pi4j.ktx.io.digital.digitalOutput
import com.pi4j.ktx.io.digital.listen
import com.pi4j.ktx.io.digital.onHigh
import com.pi4j.ktx.io.digital.onLow
import com.pi4j.ktx.pi4j
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import org.kikermo.matrix8.domain.model.Preset
import java.lang.Thread.sleep

actual class Matrix8GPIOService(
    private val presetFlow: MutableStateFlow<Preset>,
    private val initialPresets: List<Preset>
) {
    actual suspend fun start() {
     //   presetFlow.collectLatest(::setLEDs)
        setupSwitches()
    }

    private fun setLEDs(preset: Preset) {
        // Clear all LEDs
        setLED(LED_PIN_RED, false)
        setLED(LED_PIN_GREEN, false)
//        setLED(LED_PIN_YELLOW,false)
//        setLED(LED_PIN_BLUE,false)

        // Set selected
        setLED(preset.toLED(), true)
    }

    private fun setLED(led: Int, enabled: Boolean) {
        val digitalState = DigitalState.getState(enabled)

        console {
            title("<-- Matrix 8 -->", "Setting LEDs")
            pi4j {
                providers().describe().print(System.out)
                digitalOutput(led) {
                    id("led")
                    name("LED")
                    shutdown(digitalState)
                    initial(digitalState)
                    //  providers().digitalOutput()
                    provider("gpiod-digital-output")
                }.run { state(digitalState) }
            }
        }
    }

    private fun Preset.toLED(): Int {
        return when (id) {
            "A" -> LED_PIN_RED
            "B" -> LED_PIN_GREEN
            "C" -> LED_PIN_YELLOW
            "E" -> LED_PIN_BLUE
            else -> LED_PIN_RED
        }
    }

    private fun setupSwitches() {
        setSwitchListener(SWITCH_PIN_1)
        setSwitchListener(SWITCH_PIN_2)
//        setSwitchListener(SWITCH_PIN_3)
//        setSwitchListener(SWITCH_PIN_4)
    }

    private fun setSwitchListener(switchPin: Int) {
        console {
            title("<-- Matrix 8 -->", "Setting Switch")
            pi4j {
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
                while (true) {
                    sleep(2000L)
                }
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
        private const val LED_PIN_BLUE = 0
        private const val LED_PIN_YELLOW = 0

        private const val SWITCH_PIN_1 = 22
        private const val SWITCH_PIN_2 = 23
        private const val SWITCH_PIN_3 = 0
        private const val SWITCH_PIN_4 = 0
    }
}