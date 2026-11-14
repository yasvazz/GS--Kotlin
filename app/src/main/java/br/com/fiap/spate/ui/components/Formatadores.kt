package br.com.fiap.spate.ui.components

import androidx.compose.ui.graphics.Color
import br.com.fiap.spate.model.NivelRisco
import br.com.fiap.spate.ui.theme.RiscoAlto
import br.com.fiap.spate.ui.theme.RiscoBaixo
import br.com.fiap.spate.ui.theme.RiscoCritico
import br.com.fiap.spate.ui.theme.RiscoModerado
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Formata o timestamp salvo no banco como "13/11/2026 20:17". */
fun formatarData(timestamp: Long): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR")).format(Date(timestamp))

/** Cor associada a cada nível de risco. */
fun corDoNivel(nivel: NivelRisco): Color = when (nivel) {
    NivelRisco.BAIXO -> RiscoBaixo
    NivelRisco.MODERADO -> RiscoModerado
    NivelRisco.ALTO -> RiscoAlto
    NivelRisco.CRITICO -> RiscoCritico
}
