package org.kikermo.matrix8.di

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import org.kikermo.matrix8.domain.GetMatrix8PedalsUseCase
import org.kikermo.matrix8.domain.SwitchPedalUseCase
import org.kikermo.matrix8.domain.model.Pedal
import org.kikermo.matrix8.domain.model.Preset
import org.kikermo.matrix8.io.Matrix8BleServiceFactory
import org.kikermo.matrix8.io.Matrix8GPIOService
import org.kikermo.matrix8.io.Matrix8I2CPeripheralFactory
import org.kikermo.matrix8.io.Matrix8I2CService
import org.kikermo.matrix8.io.MatrixPersister
import org.kikermo.matrix8.presentation.Matrix8ViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module


val matrix8Module = module {
    single<MutableStateFlow<List<Pedal>>>(named<Pedal>()) { MutableStateFlow(initialPedalList) }
    single<MutableStateFlow<Preset>>(named<Preset>()) { MutableStateFlow(presets.first()) }

    single<StateFlow<Preset>>(named<Preset>()) {
        val presetFlow: MutableStateFlow<Preset> = get(named<Preset>())
        presetFlow.asStateFlow()
    }

    single<Flow<List<Pedal>>>(named<Pedal>()) {
        val pedalFlow: MutableStateFlow<List<Pedal>> = get(named<Pedal>())
        val presetFlow: MutableStateFlow<Preset> = get(named<Preset>())
        merge(pedalFlow,presetFlow.map { it.pedals })
    }

    single {
        SwitchPedalUseCase(
            mutablePedalListStateFlow = get(named<Pedal>())
        )
    }
    single {
        GetMatrix8PedalsUseCase(
            pedalsFlow = get(named<Pedal>()),
        )
    }

    singleOf(::Matrix8ViewModel)

    single { Matrix8I2CPeripheralFactory().create() }
    single {
        Matrix8BleServiceFactory(
            initialPedalList = initialPedalList,
            presets = presets,
            pedalStateFlow = get(named<Pedal>()),
            presetStateFlow = get(named<Preset>()),
        ).create()
    }

    singleOf(::MatrixPersister)
    single {
        Matrix8I2CService(
            matrixPersister = get(),
            pedalsFlow = get(named<Pedal>()),
            i2CPeripheral = get()
        )
    }
    single {
        Matrix8GPIOService(
            presetFlow = get(named<Preset>())
        )
    }
    singleOf(::Matrix8GPIOService)
}
