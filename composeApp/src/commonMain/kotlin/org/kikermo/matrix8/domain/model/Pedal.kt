package org.kikermo.matrix8.domain.model

import androidx.compose.ui.graphics.Color

data class Pedal(
    val ioChannel: Int,
    val text: String,
    val textColour: Color,
    val bgColour: Color,
    val enabled: Boolean,
)