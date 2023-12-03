package org.kikermo.matrix8.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.kikermo.matrix8.interactors.GetMatrix8PedalsUseCase
import org.kikermo.matrix8.interactors.SetMatrix8UseCase
import org.kikermo.matrix8.model.Pedal
import javax.inject.Inject

class Matrix8ViewModel @Inject constructor(
    private val setMatrix8UseCase: SetMatrix8UseCase,
    private val getMatrix8Pedals: GetMatrix8PedalsUseCase,
) {
    init {
        CoroutineScope(Dispatchers.Main).launch {
            val pedals = setMatrix8UseCase(getMatrix8Pedals())
            _mutableViewState.value = ViewState.PedalsLoaded(pedals)
        }
    }

    private val _mutableViewState = MutableStateFlow<ViewState>(ViewState.Loading)
    val viewState: StateFlow<ViewState>
        get() = _mutableViewState.asStateFlow()

    fun enablePedal(enabled: Boolean, pedal: Pedal) {
        // i2c toggle
        CoroutineScope(Dispatchers.Main).launch {
            val pedals = getMatrix8Pedals(Pair(pedal, enabled))

            _mutableViewState.value = (ViewState.PedalsLoaded(pedals))

            setMatrix8UseCase(pedals)
        }
    }

    sealed class ViewState {
        data class PedalsLoaded(
            val pedals: List<Pedal>
        ) : ViewState()

        data object Loading: ViewState()
    }
}
