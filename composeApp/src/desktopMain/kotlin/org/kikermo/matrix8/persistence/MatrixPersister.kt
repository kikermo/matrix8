package org.kikermo.matrix8.persistence

import org.kikermo.matrix8.di.Inject

class MatrixPersister @Inject constructor() {
    private var matrix = Array(8) { BooleanArray(8) }

    fun getPreviousMatrix(): Array<BooleanArray> {
        return matrix
    }

    fun updateMatrix(newMatrix: Array<BooleanArray>) {
        matrix = newMatrix
    }
}
