package org.kikermo.matrix8.presentation

import androidx.compose.runtime.Composable
import org.kikermo.matrix8.di.i2CPeripheralBuilder
import org.kikermo.matrix8.domain.GetMatrix8PedalsUseCase
import org.kikermo.matrix8.domain.SetMatrix8UseCase
import org.kikermo.matrix8.repository.I2CPeripheral
import org.kikermo.matrix8.repository.persistence.MatrixPersister


@Composable
fun matrix8ViewmodelFactory(): Matrix8ViewModel {
    // TODO figure out DI for compose desktop
    return Matrix8ViewModel(
        setMatrix8UseCase = SetMatrix8UseCase(
            matrixPersister = MatrixPersister(),
            i2CPeripheral = i2CPeripheralBuilder()
        ),
        getMatrix8Pedals = GetMatrix8PedalsUseCase(),
    )
}
