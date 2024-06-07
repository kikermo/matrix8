package org.kikermo.matrix8.io


class MatrixPersister{
    private var matrix = Array(8) { BooleanArray(8) }

    fun getPreviousMatrix(): Array<BooleanArray> {
        return matrix
    }

    fun updateMatrix(newMatrix: Array<BooleanArray>) {
        matrix = newMatrix
    }
}
