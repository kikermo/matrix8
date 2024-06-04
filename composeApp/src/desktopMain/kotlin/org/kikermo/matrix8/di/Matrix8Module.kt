package org.kikermo.matrix8.di

import org.kikermo.matrix8.domain.GetMatrix8PedalsUseCase
import org.kikermo.matrix8.domain.SetMatrix8UseCase
import org.kikermo.matrix8.io.Matrix8I2CPeripheralFactory
import org.kikermo.matrix8.presentation.Matrix8ViewModel
import org.kikermo.matrix8.repository.persistence.MatrixPersister
import org.koin.dsl.module

val matrix8Module = module {
//    single { Matrix8I2CPeripheralFactory().create() }
//    singleOf(::SetMatrix8UseCase)
//    singleOf(::GetMatrix8PedalsUseCase)
//    singleOf(::Matrix8ViewModel)
    single {
        Matrix8ViewModel(
            setMatrix8UseCase = SetMatrix8UseCase(
                matrixPersister = MatrixPersister(),
                i2CPeripheral = Matrix8I2CPeripheralFactory().create()
            ),
            getMatrix8Pedals = GetMatrix8PedalsUseCase()
        )
    }
}
