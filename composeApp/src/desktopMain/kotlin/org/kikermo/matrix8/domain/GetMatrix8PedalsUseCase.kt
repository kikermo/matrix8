package org.kikermo.matrix8.domain

import androidx.compose.ui.graphics.Color
import org.kikermo.matrix8.di.Inject
import org.kikermo.matrix8.domain.model.Pedal

class GetMatrix8PedalsUseCase @Inject constructor(
) {

    private val initialPedals = listOf(
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
//        Pedal(
//            ioChannel = 3,
//            text = "Booster",
//            textColour = Color(0xFFFFFFFF).value,
//            enabled = false,
//            bgColour = Color(0xFF3F51B5).value
//        ),
        Pedal(
            ioChannel = 3,
            text = "ShimVerb",
            textColour = Color(0xFFFFFFFF).value,
            enabled = false,
            bgColour = Color(0xFF424242).value
        ),
    )

    operator fun invoke(switchedPedal: Pair<Pedal, Boolean>? = null): List<Pedal> {
        if (switchedPedal == null)
            return initialPedals

        return initialPedals.map { pedal ->
            if (pedal == switchedPedal.first) pedal.copy(enabled = switchedPedal.second) else pedal
        }
    }
}