package org.kikermo.matrix8.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.kikermo.matrix8.domain.model.Pedal

class SwitchPedalUseCase(
    private val pedalFlow: StateFlow<List<Pedal>>,
    private val mutablePedalListStateFlow: MutableStateFlow<List<Pedal>>
) {
    suspend operator fun invoke(pedal: Pedal, enabled: Boolean) {
        val currentPedals = mutablePedalListStateFlow.value
        currentPedals.map { currentPedal ->
            if (currentPedal == pedal) currentPedal.copy(enabled = enabled) else currentPedal
        }.let { mutablePedalListStateFlow.value = it }
    }
}
