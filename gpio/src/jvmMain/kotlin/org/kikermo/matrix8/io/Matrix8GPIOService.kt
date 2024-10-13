package org.kikermo.matrix8.io

import com.pi4j.io.gpio.digital.DigitalState
import com.pi4j.ktx.console
import com.pi4j.ktx.io.digital.digitalOutput
import com.pi4j.ktx.pi4j
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import org.kikermo.matrix8.domain.model.Preset

actual class Matrix8GPIOService(
    private val presetFlow: Flow<Preset>
) {
    actual suspend fun start() {
        presetFlow.collectLatest(::setLEDs)
    }

    private fun setLEDs(preset: Preset) {
        // Clear all LEDs
        setLED(LED_PIN_RED,false)
        setLED(LED_PIN_GREEN,false)
//        setLED(LED_PIN_YELLOW,false)
//        setLED(LED_PIN_BLUE,false)

        // Set selected
        setLED(preset.toLED(), true)
    }
    private fun setLED(led:Int, enabled:Boolean) {
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
        return when(id) {
            "A" -> LED_PIN_RED
            "B" -> LED_PIN_GREEN
            "C" -> LED_PIN_YELLOW
            "E" -> LED_PIN_BLUE
            else -> LED_PIN_RED
        }
    }

    companion object {
        private const val LED_PIN_RED = 27
        private const val LED_PIN_GREEN = 17
        private const val LED_PIN_BLUE = 1
        private const val LED_PIN_YELLOW = 1
    }
}