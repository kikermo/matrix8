package org.kikermo.matrix8.io

import kotlinx.coroutines.flow.MutableStateFlow
import org.kikermo.matrix8.domain.model.Pedal

actual class Matrix8BleServiceFactory(
    private val initialPedalList: List<Pedal>,
    private val mutableStateFlow: MutableStateFlow<List<Pedal>>
) {
    actual fun create(): Matrix8BleService = Matrix8BleServiceImpl(
        initialPedals = initialPedalList,
        pedalStateFlow = mutableStateFlow
    )
}