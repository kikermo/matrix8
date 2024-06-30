package org.kikermo.matrix8.io

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kikermo.matrix8.domain.model.Pedal


actual class Matrix8I2CService(
    private val i2CPeripheral: Matrix8I2CPeripheral,
    private val pedalsFlow: StateFlow<List<Pedal>>,
    private val matrixPersister: MatrixPersister,
) {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            pedalsFlow.collectLatest(::setMatrix)
        }
    }

    private suspend fun setMatrix(pedals: List<Pedal>) {
        try {
            val commands = getCommandValueList(pedals)

            i2CPeripheral.sendData(commands)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getCommandValueList(pedals: List<Pedal>): List<Byte> {
        val oldMatrix = matrixPersister.getPreviousMatrix()
        val newMatrix = getMatrix(pedals)
        matrixPersister.updateMatrix(newMatrix)

        val addressByteList = getAddressByteList(oldMatrix, newMatrix)

        return addressByteList
            .map(AddressByte::toByte)
            .mapIndexed { index, byte ->
                if (addressByteList.lastIndex == index) {  // Last element?
                    listOf(byte, DATA_0_END.toByte())
                } else {
                    listOf(byte, DATA_0_CONTINUE.toByte())
                }
            }.flatten()
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
        private const val DATA_0_END = 0b00000001 // X X X X X X X LDSW
        private const val DATA_0_CONTINUE = 0b00000000 // X X X X X X X LDSW
    }
}