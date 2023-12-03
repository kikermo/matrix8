package org.kikermo.matrix8.presentation

import androidx.compose.runtime.Composable
import org.kikermo.matrix8.interactors.GetMatrix8PedalsUseCase
import org.kikermo.matrix8.interactors.SetMatrix8UseCase
import org.kikermo.matrix8.persistence.MatrixPersister


@Composable
inline fun matrix8ViewmodelFactory(): Matrix8ViewModel {
    // TODO figure out DI for compose desktop
    return Matrix8ViewModel(
        setMatrix8UseCase = SetMatrix8UseCase(MatrixPersister()),
        getMatrix8Pedals = GetMatrix8PedalsUseCase(),
    )
}