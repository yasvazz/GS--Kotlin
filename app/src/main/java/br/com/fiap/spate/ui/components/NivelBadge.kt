package br.com.fiap.spate.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.fiap.spate.model.NivelRisco

/** Etiqueta colorida com o nível de risco (ex.: "Alto"). */
@Composable
fun NivelBadge(nivel: NivelRisco, modifier: Modifier = Modifier, texto: String = nivel.rotulo) {
    val cor = corDoNivel(nivel)
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = cor.copy(alpha = 0.15f)
    ) {
        Text(
            text = texto,
            color = cor,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
