package org.kikermo.matrix8.io

import com.pi4j.Pi4J
import com.pi4j.io.IOType
import com.pi4j.io.gpio.digital.DigitalInputConfig
import com.pi4j.io.gpio.digital.DigitalInputProvider
import com.pi4j.io.gpio.digital.DigitalState
import com.pi4j.ktx.io.digital.listen
import com.pi4j.platform.Platform
import kotlinx.coroutines.flow.MutableStateFlow


class GPIOButtonReadingSample(
    buttonStatePublisher: MutableStateFlow<ButtonState>
) {
    companion object {
        private const val DIGITAL_INPUT_PIN: Int = 3
    }

    private val pi4j = Pi4J.newAutoContext()
    private val platform: Platform = pi4j.platforms().getDefault()
    private val provider: DigitalInputProvider = platform.provider(IOType.DIGITAL_INPUT)
    private val config = DigitalInputConfig.newBuilder(pi4j).address(DIGITAL_INPUT_PIN).build()
    private val input = provider.create(config)


    init {
        input.listen { event ->
            val buttonState = when (event.state()) {
                DigitalState.LOW -> ButtonState.ON
                DigitalState.HIGH,
                DigitalState.UNKNOWN -> ButtonState.OFF
            }
            buttonStatePublisher.tryEmit(buttonState)
        }
    }

    fun onShutdown() {
        pi4j.shutdown()
    }
}

enum class ButtonState {
    ON, OFF
}
