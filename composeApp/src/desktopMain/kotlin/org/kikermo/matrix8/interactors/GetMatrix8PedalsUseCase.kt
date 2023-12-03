package org.kikermo.matrix8.interactors

import androidx.compose.ui.graphics.Color
import org.kikermo.matrix8.model.Pedal
import org.kikermo.matrix8.persistence.MatrixPersister
import javax.inject.Inject

class GetMatrix8PedalsUseCase @Inject constructor(
    private val matrixPersister: MatrixPersister
) {

    private val initialPedals = listOf(
        Pedal(
            ioChannel = 1,
            text = "Tube Screamer",
            textColour = Color.White,
            enabled = false,
            bgColour = Color(0xff4ea541)
        ),
        Pedal(
            ioChannel = 2,
            text = "Blue Crab",
            textColour = Color(0xFF2196F3),
            enabled = false,
            bgColour = Color.Black
        ),
        Pedal(
            ioChannel = 3,
            text = "Booster",
            textColour = Color(0xFFFFFFFF),
            enabled = false,
            bgColour = Color(0xFF3F51B5)
        ),
        Pedal(
            ioChannel = 4,
            text = "ShimVerb",
            textColour = Color(0xFFFFFFFF),
            enabled = false,
            bgColour = Color(0xFF424242)
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