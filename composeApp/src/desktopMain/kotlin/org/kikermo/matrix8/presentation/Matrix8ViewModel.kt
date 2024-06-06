package org.kikermo.matrix8.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.kikermo.matrix8.domain.GetMatrix8PedalsUseCase
import org.kikermo.matrix8.domain.SetMatrix8UseCase
import org.kikermo.matrix8.domain.model.Pedal
import org.kikermo.matrix8.io.Matrix8BleService

class Matrix8ViewModel(
    private val setMatrix8UseCase: SetMatrix8UseCase,
    private val getMatrix8Pedals: GetMatrix8PedalsUseCase,
    private val bleService: Matrix8BleService
) {
    private val _mutableViewState = MutableStateFlow<ViewState>(ViewState.Loading)
    val viewState: StateFlow<ViewState>
        get() = _mutableViewState.asStateFlow()


    init {
        CoroutineScope(Dispatchers.Default).launch {
            val pedals = getMatrix8Pedals()
            _mutableViewState.value = ViewState.PedalsLoaded(pedals)
        }
        CoroutineScope(Dispatchers.IO).launch {
            bleService.startService()
            bleService.setCallback {
                println("Value $it")
            }
        }
    }

    fun enablePedal(enabled: Boolean, pedal: Pedal) {
        // i2c toggle
        CoroutineScope(Dispatchers.Default).launch {
            val pedals = getMatrix8Pedals(Pair(pedal, enabled))

            _mutableViewState.value = (ViewState.PedalsLoaded(pedals))

            setMatrix8UseCase(pedals)
        }
    }

    sealed class ViewState {
        data class PedalsLoaded(
            val pedals: List<Pedal>
        ) : ViewState()

        data object Loading : ViewState()
    }
}
