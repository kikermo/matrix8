package org.kikermo.matrix8.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.kikermo.matrix8.domain.model.Pedal
import org.kikermo.matrix8.io.Matrix8I2CPeripheral
import org.kikermo.matrix8.repository.persistence.MatrixPersister

class SetMatrix8UseCase(
    private val matrixPersister: MatrixPersister,
    private val i2CPeripheral: Matrix8I2CPeripheral,
) {
    suspend operator fun invoke(pedals: List<Pedal>): List<Pedal> {
        return withContext(Dispatchers.IO) {
            try {
                val commands = getCommandValueList(pedals)
                if (commands.isEmpty()) {
                    return@withContext pedals
                }

                i2CPeripheral.open()
                i2CPeripheral.sendData(commands)
                i2CPeripheral.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext pedals
        }
    }


    private fun getCommandValueList(pedals: List<Pedal>): List<Pair<Byte, Byte>> {
        val oldMatrix = matrixPersister.getPreviousMatrix()
        val newMatrix = getMatrix(pedals)
        matrixPersister.updateMatrix(newMatrix)

        val addressByteList = getAddressByteList(oldMatrix, newMatrix)

        return addressByteList
            .map(AddressByte::toByte)
            .mapIndexed { index, byte ->
                if (addressByteList.lastIndex == index) {  // Last element?
                    Pair(byte, DATA_0_END.toByte())
                } else {
                    Pair(byte, DATA_0_CONTINUE.toByte())
                }
            }
    }

    private fun getAddressByteList(
        oldMatrix: Array<BooleanArray>,
        newMatrix: Array<BooleanArray>
    ): List<AddressByte> {
        val addressesPairList = mutableListOf<AddressByte>()

        for (x in oldMatrix.indices) {
            for (y in oldMatrix[x].indices) {
                if (oldMatrix[x][y] != newMatrix[x][y]) {
                    addressesPairList.add(
                        AddressByte(
                            enabled = newMatrix[x][y],
                            x = x,
                            y = y,
                        )
                    )
                }
            }
        }

        return addressesPairList.toList()
    }

    private fun getMatrix(pedals: List<Pedal>): Array<BooleanArray> {
        val enabledPedals = pedals.filter { it.enabled }
        val matrix = Array(8) { BooleanArray(8) }
        if (enabledPedals.isEmpty()) {
            matrix[0][0] = true
            return matrix
        }
        if (enabledPedals.size == 1) {
            matrix[0][enabledPedals.first().ioChannel] = true
            matrix[enabledPedals.first().ioChannel][0] = true
            return matrix
        }
        enabledPedals
            .forEachIndexed { index, pedal ->
                when (index) {
                    0 -> {
                        matrix[0][pedal.ioChannel] = true
                        matrix[pedal.ioChannel][enabledPedals[index + 1].ioChannel] = true
                    }

                    enabledPedals.lastIndex -> matrix[pedal.ioChannel][0] = true
                    else -> matrix[pedal.ioChannel][enabledPedals[index + 1].ioChannel] = true
                }
            }

        return matrix
    }

    private data class AddressByte(
        val enabled: Boolean,
        val x: Int,
        val y: Int,
    ) {
        fun toByte(): Byte {
            val enableBit = if (enabled) 1 else 0
            return enableBit.shl(7).or(x.xAddressOffset().shl(3)).or(y).toByte()
        }

        private fun Int.xAddressOffset(): Int {
            return if (this < 6) return this else (this + 2)  // 0bX0110000 and 0bX0111000 are reserved
        }
    }

    companion object {
        //        private const val DEVICE_ADDRESS = 0b1110111 // 1 1 1 0 a2 a1 a0 R/W, r/w required?
        private const val DATA_0_END = 0b00000001 // X X X X X X X LDSW
        private const val DATA_0_CONTINUE = 0b00000000 // X X X X X X X LDSW
    }

}