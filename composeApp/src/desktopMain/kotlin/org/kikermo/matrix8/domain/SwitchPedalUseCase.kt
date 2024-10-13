package org.kikermo.matrix8.domain

import kotlinx.coroutines.flow.MutableStateFlow
import org.kikermo.matrix8.domain.model.Pedal
import org.kikermo.matrix8.domain.model.Preset

class SwitchPedalUseCase(
    private val mutablePresetStateFlow: MutableStateFlow<Preset>
) {
    operator fun invoke(pedal: Pedal, enabled: Boolean) {
        val currentPreset = mutablePresetStateFlow.value
        val modifiedPedals = currentPreset.pedals.map { currentPedal ->
            if (currentPedal == pedal) currentPedal.copy(enabled = enabled) else currentPedal
        }
        mutablePresetStateFlow.value = currentPreset.copy(pedals = modifiedPedals)
    }
}
