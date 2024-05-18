package org.kikermo.matrix8.domain.model

data class Pedal(
    val ioChannel: Int,
    val text: String,
    val textColour: ULong,
    val bgColour: ULong,
    val enabled: Boolean,
)