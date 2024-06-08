package org.kikermo.matrix8.di

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.kikermo.matrix8.domain.GetMatrix8PedalsUseCase
import org.kikermo.matrix8.domain.SwitchPedalUseCase
import org.kikermo.matrix8.domain.model.Pedal
import org.kikermo.matrix8.io.Matrix8BleServiceFactory
import org.kikermo.matrix8.io.Matrix8I2CPeripheralFactory
import org.kikermo.matrix8.io.Matrix8I2CService
import org.kikermo.matrix8.io.MatrixPersister
import org.kikermo.matrix8.presentation.Matrix8ViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val matrix8Module = module {
    single { Matrix8I2CPeripheralFactory().create() }
    single {
        Matrix8BleServiceFactory(
            initialPedalList = get(),
            mutableStateFlow = get(),
        ).create()
    }
    single<MutableStateFlow<List<Pedal>>> { MutableStateFlow(get()) }
    single<StateFlow<List<Pedal>>> {
        val stateFlow: MutableStateFlow<List<Pedal>> = get()
        stateFlow.asStateFlow()
    }
    singleOf(::SwitchPedalUseCase)
    singleOf(::MatrixPersister)
    singleOf(::GetMatrix8PedalsUseCase)
    singleOf(::Matrix8ViewModel)
    singleOf(::Matrix8I2CService)

    single {
        getInitialPedalList()
    }
}
