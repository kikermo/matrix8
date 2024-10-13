package org.kikermo.matrix8.di

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.kikermo.matrix8.domain.GetMatrix8PedalsUseCase
import org.kikermo.matrix8.domain.SwitchPedalUseCase
import org.kikermo.matrix8.domain.model.Preset
import org.kikermo.matrix8.io.Matrix8BleServiceFactory
import org.kikermo.matrix8.io.Matrix8GPIOService
import org.kikermo.matrix8.io.Matrix8I2CPeripheralFactory
import org.kikermo.matrix8.io.Matrix8I2CService
import org.kikermo.matrix8.io.MatrixPersister
import org.kikermo.matrix8.presentation.Matrix8ViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val matrix8Module = module {
    single<MutableStateFlow<Preset>> { MutableStateFlow(presets.first()) }

    single<Flow<Preset>> {
        val presetFlow: MutableStateFlow<Preset> = get()
        presetFlow.asStateFlow()
    }

    singleOf(::SwitchPedalUseCase)
    singleOf(::GetMatrix8PedalsUseCase)

    singleOf(::Matrix8ViewModel)

    singleOf(::MatrixPersister)
    singleOf(::Matrix8I2CService)
    singleOf(::Matrix8GPIOService)
    single { Matrix8I2CPeripheralFactory().create() }
    single {
        Matrix8BleServiceFactory(
            initialPedalList = initialPedalList,
            presets = presets,
            presetStateFlow = get(),
        ).create()
    }
}
