package org.kikermo.matrix8.io

import com.pi4j.io.gpio.digital.DigitalState
import com.pi4j.ktx.console
import com.pi4j.ktx.io.digital.digitalOutput
import com.pi4j.ktx.pi4j
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kikermo.matrix8.domain.model.Pedal

actual class Matrix8GPIOService(
    private val pedalsFlow: MutableStateFlow<List<Pedal>>,
) {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            pedalsFlow.collectLatest(::setLED)
        }
    }

    private fun setLED(pedals: List<Pedal>) {
        val digitalState = pedals.firstOrNull()?.enabled?.let(DigitalState::getState)
            ?: return
        console {
            title("<-- Matrix 8 -->", "Setting Pins")
            pi4j {
                digitalOutput(LED_PIN_RED) {
                    id("led")
                    name("LED Flasher")
                    shutdown(digitalState)
                    initial(digitalState)
                    provider("gpiod-digital-output")
                }.run { state(digitalState) }
            }
        }
    }

    companion object {
        private const val LED_PIN_RED = 17
        private const val LED_PIN_GREEN = 27
    }
}