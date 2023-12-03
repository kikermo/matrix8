package org.kikermo.matrix8.presentation

import androidx.compose.runtime.Composable
import dagger.hilt.EntryPoints
import org.kikermo.matrix8.AppComponent


@Composable
inline fun matrix8ViewmodelFactory(): Matrix8ViewModel {
    // TODO figure out DI for compose desktop
    return EntryPoints.get(AppComponent(), Matrix8ViewModel::class.java)
}