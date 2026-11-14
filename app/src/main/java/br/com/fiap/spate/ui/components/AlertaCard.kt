package br.com.fiap.spate.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.fiap.spate.data.Alerta
import br.com.fiap.spate.data.nivelEnum
import br.com.fiap.spate.data.tipoEnum
import br.com.fiap.spate.ui.theme.RiscoBaixo

/** Card de um alerta na listagem, com botões de favoritar e marcar como resolvido. */
@Composable
fun AlertaCard(
    alerta: Alerta,
    onClick: () -> Unit,
    onAlternarResolvido: () -> Unit,
    onAlternarFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tipo = alerta.tipoEnum()

    Card(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = tipo.icone, style = MaterialTheme.typography.headlineMedium)

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "${tipo.rotulo} • ${alerta.bairro}",
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (alerta.resolvido) TextDecoration.LineThrough else null,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = alerta.descricao,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NivelBadge(nivel = alerta.nivelEnum())
                    Text(
                        text = if (alerta.resolvido) "Resolvido" else formatarData(alerta.dataCriacao),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (alerta.resolvido) RiscoBaixo else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column {
                IconButton(onClick = onAlternarFavorito) {
                    Icon(
                        imageVector = if (alerta.favorito) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = if (alerta.favorito) "Remover dos favoritos" else "Favoritar",
                        tint = if (alerta.favorito) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onAlternarResolvido) {
                    Icon(
                        imageVector = if (alerta.resolvido) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                        contentDescription = if (alerta.resolvido) "Reativar alerta" else "Marcar como resolvido",
                        tint = if (alerta.resolvido) RiscoBaixo
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
