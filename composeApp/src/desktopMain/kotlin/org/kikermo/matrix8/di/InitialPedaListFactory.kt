package org.kikermo.matrix8.di

import androidx.compose.ui.graphics.Color
import org.kikermo.matrix8.domain.model.Pedal

fun getInitialPedalList() = listOf(
    Pedal(
        ioChannel = 1,
        text = "Tube Screamer",
        textColour = Color.White.value,
        enabled = false,
        bgColour = Color(0xff4ea541).value
    ),
    Pedal(
        ioChannel = 2,
        text = "Blue Crab",
        textColour = Color(0xFF2196F3).value,
        enabled = false,
        bgColour = Color.Black.value
    ),
    Pedal(
        ioChannel = 3,
        text = "Booster",
        textColour = Color(0xFFFFFFFF).value,
        enabled = false,
        bgColour = Color(0xFF3F51B5).value
    ),
    Pedal(
        ioChannel = 4,
        text = "ShimVerb",
        textColour = Color(0xFFFFFFFF).value,
        enabled = false,
        bgColour = Color(0xFF424242).value
    ),
    Pedal(
        ioChannel = 5,
        text = "Digital Delay",
        textColour = Color(0xFF2196F3).value,
        enabled = false,
        bgColour = Color(0xFFAAAAAA).value
    ),
    Pedal(
        ioChannel = 6,
        text = "Plexi Sim",
        textColour = Color(0xFFffaa00).value,
        enabled = false,
        bgColour = Color(0xFF8a712c).value
    ),
)