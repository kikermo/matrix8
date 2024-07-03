package org.kikermo.matrix8.io

import com.pi4j.io.gpio.digital.DigitalState
import com.pi4j.ktx.io.digital.digitalOutput
import com.pi4j.ktx.io.digital.piGpioProvider
import com.pi4j.ktx.pi4j

class GPIOLEDExample {
    companion object {
        private const val DIGITAL_OUTPUT_PIN_SOUND_A: Int = 26 // GPIO 6
        private const val DIGITAL_OUTPUT_PIN_SOUND_B: Int = 31  // GPIO 7
    }

    fun setLED(button: MatrixButton, on: Boolean) {
        pi4j {
            val pin = when (button) {
                MatrixButton.SOUND_A -> DIGITAL_OUTPUT_PIN_SOUND_A
                MatrixButton.SOUND_B -> DIGITAL_OUTPUT_PIN_SOUND_B
            }
            val digitalState = when (on) {
                true -> DigitalState.HIGH
                false -> DigitalState.LOW
            }
            digitalOutput(pin) {
                initial(digitalState)
                shutdown(digitalState)
                piGpioProvider()
            }
        }
    }
}

enum class MatrixButton {
    SOUND_A, SOUND_B
}

