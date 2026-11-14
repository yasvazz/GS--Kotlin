package br.com.fiap.spate.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EsquemaClaro = lightColorScheme(
    primary = AzulSpate,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = AzulSpateClaro,
    onPrimaryContainer = AzulSpateEscuro,
    secondary = LaranjaAlerta,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = LaranjaAlertaClaro
)

private val EsquemaEscuro = darkColorScheme(
    primary = AzulSpateNoite,
    onPrimary = AzulSpateEscuro,
    primaryContainer = AzulSpate,
    onPrimaryContainer = AzulSpateClaro,
    secondary = LaranjaAlertaNoite,
    background = FundoNoite,
    surface = FundoNoite
)

@Composable
fun SpateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaEscuro else EsquemaClaro,
        typography = Typography,
        content = content
    )
}
