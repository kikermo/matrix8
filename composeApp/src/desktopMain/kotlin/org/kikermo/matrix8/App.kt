package org.kikermo.matrix8

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.kikermo.matrix8.domain.model.Pedal
import org.kikermo.matrix8.presentation.Matrix8ViewModel
import org.kikermo.matrix8.ui.components.PedalItem
import org.kikermo.matrix8.ui.theme.ModularTheme
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
fun App() {
    KoinContext {
        val viewModel = koinInject<Matrix8ViewModel>()

        MaterialTheme {
            val viewState = viewModel.viewState.collectAsState().value

            ModularTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    when (viewState) {
                        Matrix8ViewModel.ViewState.Loading -> Loading()
                        is Matrix8ViewModel.ViewState.PedalsLoaded -> PedalsLoaded(viewState) { pedal, enabled ->
                            viewModel.enablePedal(enabled, pedal)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PedalsLoaded(
    viewState: Matrix8ViewModel.ViewState.PedalsLoaded,
    onPedalToggled: (pedal: Pedal, enabled: Boolean) -> Unit
) {
    LazyRow {
        items(viewState.pedals) { pedal ->
            PedalItem(
                text = pedal.text,
                enabled = pedal.enabled,
                textColour = Color(pedal.textColour),
                bgColour = Color(pedal.bgColour),
                clickListener = { onPedalToggled(pedal, !pedal.enabled) }
            )
        }
    }
}

@Composable
fun Loading() {
    Box {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}