package org.kikermo.matrix8.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.kikermo.matrix8.domain.model.Pedal

class GetMatrix8PedalsUseCase(
    private val pedalsStateFlow: StateFlow<List<Pedal>>
) {
    operator fun invoke(): Flow<List<Pedal>> {
        return pedalsStateFlow
    }
}