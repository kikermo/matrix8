package org.kikermo.matrix8.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kikermo.matrix8.domain.GetMatrix8PedalsUseCase
import org.kikermo.matrix8.domain.SwitchPedalUseCase
import org.kikermo.matrix8.domain.model.Pedal

class Matrix8ViewModel(
    private val getMatrix8Pedals: GetMatrix8PedalsUseCase,
    private val switchPedalUseCase: SwitchPedalUseCase
) {
    private val _mutableViewState = MutableStateFlow<ViewState>(ViewState.Loading)
    val viewState: StateFlow<ViewState>
        get() = _mutableViewState.asStateFlow()


    init {
        CoroutineScope(Dispatchers.Default).launch {
            getMatrix8Pedals().collectLatest { pedals ->
                _mutableViewState.value = ViewState.PedalsLoaded(pedals)
            }
        }
    }

    fun enablePedal(enabled: Boolean, pedal: Pedal) {
        // pedal toggle
        CoroutineScope(Dispatchers.Default).launch {
            switchPedalUseCase(pedal = pedal, enabled = enabled)
        }
    }

    sealed class ViewState {
        data class PedalsLoaded(
            val pedals: List<Pedal>
        ) : ViewState()

        data object Loading : ViewState()
    }
}
