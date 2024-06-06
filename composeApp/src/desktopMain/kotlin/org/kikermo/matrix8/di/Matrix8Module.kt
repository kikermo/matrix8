package org.kikermo.matrix8.di

import org.kikermo.matrix8.domain.GetMatrix8PedalsUseCase
import org.kikermo.matrix8.domain.SetMatrix8UseCase
import org.kikermo.matrix8.io.Matrix8BleServiceFactory
import org.kikermo.matrix8.io.Matrix8I2CPeripheralFactory
import org.kikermo.matrix8.presentation.Matrix8ViewModel
import org.kikermo.matrix8.repository.persistence.MatrixPersister
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val matrix8Module = module {
    single { Matrix8I2CPeripheralFactory().create() }
    single { Matrix8BleServiceFactory().create() }
    singleOf(::SetMatrix8UseCase)
    singleOf(::MatrixPersister)
    singleOf(::GetMatrix8PedalsUseCase)
    singleOf(::Matrix8ViewModel)
}
