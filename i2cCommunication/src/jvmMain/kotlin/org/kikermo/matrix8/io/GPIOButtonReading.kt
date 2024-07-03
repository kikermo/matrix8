package org.kikermo.matrix8.io

import com.pi4j.context.Context
import com.pi4j.io.gpio.digital.PullResistance
import com.pi4j.ktx.console
import com.pi4j.ktx.io.digital.digitalInput
import com.pi4j.ktx.io.digital.onLow
import com.pi4j.ktx.io.digital.piGpioProvider
import com.pi4j.ktx.pi4jAsync


class GPIOButtonReading {
    companion object {
        private const val DIGITAL_INPUT_PIN_SOUND_A: Int = 7 // GPIO 4
        private const val DIGITAL_INPUT_PIN_SOUND_B: Int = 29  // GPIO 5
    }

    fun setButtonListener(onButtonPressed: (matrixButton: MatrixButton) -> Unit) {
        pi4jAsync {
            console {
                matrix8Button(DIGITAL_INPUT_PIN_SOUND_A) { onButtonPressed(MatrixButton.SOUND_A) }
                matrix8Button(DIGITAL_INPUT_PIN_SOUND_B) { onButtonPressed(MatrixButton.SOUND_B) }
            }
        }
    }

    private fun Context.matrix8Button(
        pin: Int
        onButtonPressed: () -> Unit,
    ) {
        digitalInput(pin) {
            pull(PullResistance.PULL_DOWN)
            debounce(1000L)
            piGpioProvider()
        }.onLow {
            onButtonPressed()
        }
    }

    enum class MatrixButton {
        SOUND_A, SOUND_B
    }
}
