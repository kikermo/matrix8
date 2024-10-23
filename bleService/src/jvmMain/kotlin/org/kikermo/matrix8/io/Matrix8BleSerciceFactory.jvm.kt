package org.kikermo.matrix8.io

import kotlinx.coroutines.flow.MutableStateFlow
import org.kikermo.matrix8.domain.model.Pedal
import org.kikermo.matrix8.domain.model.Preset

actual class Matrix8BleServiceFactory(
    private val initialPedalList: List<Pedal>,
    private val presets: List<Preset>,
    private val presetStateFlow: MutableStateFlow<Preset>,
) {
    actual fun create(): Matrix8BleService = Matrix8BleServiceImpl(
        initialPedals = initialPedalList,
        presetStateFlow = presetStateFlow,
        initialPresets = presets
    )
}