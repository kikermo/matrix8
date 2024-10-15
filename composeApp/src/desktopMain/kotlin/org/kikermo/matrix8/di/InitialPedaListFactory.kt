package org.kikermo.matrix8.di

import androidx.compose.ui.graphics.Color
import org.kikermo.matrix8.domain.model.Pedal
import org.kikermo.matrix8.domain.model.Preset


private val pedal1 = Pedal(
    ioChannel = 1,
    text = "Tube Screamer",
    textColour = Color.White.value,
    enabled = false,
    bgColour = Color(0xff4ea541).value
)

private val pedal2 = Pedal(
    ioChannel = 2,
    text = "Blue Crab",
    textColour = Color(0xFF2196F3).value,
    enabled = false,
    bgColour = Color.Black.value
)

private val pedal3 = Pedal(
    ioChannel = 3,
    text = "Booster",
    textColour = Color(0xFFFFFFFF).value,
    enabled = false,
    bgColour = Color(0xFF3F51B5).value
)
private val pedal4 = Pedal(
    ioChannel = 4,
    text = "ShimVerb",
    textColour = Color(0xFFFFFFFF).value,
    enabled = false,
    bgColour = Color(0xFF424242).value
)

val initialPedalList = listOf(pedal1, pedal2, pedal3, pedal4)

val presets = listOf(
    Preset(
        id = "A",
        pedals = initialPedalList.enanablePedals()
    ),
    Preset(
        id = "B",
        pedals = listOf(pedal2, pedal4).enanablePedals()
    ),
    Preset(
        id = "C",
        pedals = listOf(pedal3, pedal1, pedal4).enanablePedals()
    ),
    Preset(
        id = "D",
        pedals = listOf(pedal4).enanablePedals()
    )
)

private fun List<Pedal>.enanablePedals() = map { it.copy(enabled = true) }