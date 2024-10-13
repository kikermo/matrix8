package org.kikermo.matrix8.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import org.kikermo.matrix8.domain.model.Pedal
import org.kikermo.matrix8.domain.model.Preset

class GetMatrix8PedalsUseCase(
    private val presetFlow: Flow<Preset>,
) {
    operator fun invoke(): Flow<List<Pedal>> {
        return presetFlow.map { it.pedals }
    }
}