package org.kikermo.matrix8.domain

import org.kikermo.matrix8.domain.model.Pedal
import kotlin.math.pow

class GetMatrix8PedalsFromByteArrayUseCase(
    private val getMatrix8PedalsUseCase: GetMatrix8PedalsUseCase
) {

    /**
     * ByteMatrix
     *
     *     x0 x1 x2 x3 x4 x5 x6 x7
     *  y0 X  X  X  X  X  X  X  X
     *  y1 X  X  X  X  X  X  X  X
     *  y2 X  X  X  X  X  X  X  X
     *  y3 X  X  X  X  X  X  X  X
     *  y4 X  X  X  X  X  X  X  X
     *  y5 X  X  X  X  X  X  X  X
     *  y6 X  X  X  X  X  X  X  X
     *  y7 X  X  X  X  X  X  X  X
     *
     *  byte[0] 0bXXXXXXXXXX
     *  byte[1] 0bXXXXXXXXXX
     *  ....................
     *  byte[7] 0bXXXXXXXXXX
     */

    operator fun invoke(byteMatrix: ByteArray): List<Pedal> {
        //  val pedals = getMatrix8PedalsUseCase.invoke().first()
        val pedals = listOf<Pedal>()
        if (byteMatrix.size != 8) {
            println("Wrong matrix length")
            return pedals
        }

        val matrix = byteMatrix.map { it.toBooleanArray() }

        if (matrix[0][0]) {
            return pedals.map { it.copy(enabled = false) }
        }

        return pedals
    }


    private fun Byte.toBooleanArray(): BooleanArray {
        return (0..7).map { i ->
            (2.0.pow(i).toInt() and this.toInt()) != 0
        }.reversed().toBooleanArray()
    }
}