package org.kikermo.matrix8.persistence

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatrixPersister @Inject constructor() {
    private var matrix = Array(8) { BooleanArray(8) }

    fun getPreviousMatrix(): Array<BooleanArray> {
        return matrix
    }

    fun updateMatrix(newMatrix: Array<BooleanArray>) {
        matrix = newMatrix
    }
}
