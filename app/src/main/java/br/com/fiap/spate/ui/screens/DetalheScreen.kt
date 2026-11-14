@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.fiap.spate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.fiap.spate.data.nivelEnum
import br.com.fiap.spate.data.tipoEnum
import br.com.fiap.spate.ui.components.EstadoVazio
import br.com.fiap.spate.ui.components.NivelBadge
import br.com.fiap.spate.ui.components.formatarData
import br.com.fiap.spate.viewmodel.AlertaViewModel

/** Tela de detalhes de um alerta, com as ações: editar, resolver, favoritar e excluir. */
@Composable
fun DetalheScreen(
    viewModel: AlertaViewModel,
    alertaId: Int,
    onVoltar: () -> Unit,
    onEditar: () -> Unit
) {
    val lista by viewModel.alertas.collectAsState()
    val alerta = lista?.firstOrNull { it.id == alertaId }
    var confirmarExclusao by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do alerta") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        when {
            lista == null -> Box(
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            alerta == null -> EstadoVazio(
                titulo = "Alerta não encontrado",
                mensagem = "Ele pode ter sido excluído.",
                modifier = Modifier.padding(padding)
            )

            else -> Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val tipo = alerta.tipoEnum()

                Text("${tipo.icone} ${tipo.rotulo}", style = MaterialTheme.typography.headlineMedium)
                Text(alerta.bairro, style = MaterialTheme.typography.titleLarge)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NivelBadge(nivel = alerta.nivelEnum(), texto = "Risco ${alerta.nivelEnum().rotulo}")
                    Text(
                        text = if (alerta.resolvido) "✅ Resolvido" else "⚠️ Ativo",
                        style = MaterialTheme.typography.labelLarge
                    )
                    if (alerta.favorito) Text("⭐ Favorito", style = MaterialTheme.typography.labelLarge)
                }

                Text(alerta.descricao, style = MaterialTheme.typography.bodyLarge)
                Text(
                    "Registrado em ${formatarData(alerta.dataCriacao)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Button(onClick = onEditar, modifier = Modifier.fillMaxWidth()) {
                    Text("Editar")
                }
                FilledTonalButton(
                    onClick = { viewModel.alternarResolvido(alerta) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (alerta.resolvido) "Reativar alerta" else "Marcar como resolvido")
                }
                OutlinedButton(
                    onClick = { viewModel.alternarFavorito(alerta) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (alerta.favorito) "Remover dos favoritos" else "Favoritar")
                }
                TextButton(
                    onClick = { confirmarExclusao = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Excluir alerta", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }

    if (confirmarExclusao && alerta != null) {
        AlertDialog(
            onDismissRequest = { confirmarExclusao = false },
            title = { Text("Excluir alerta?") },
            text = { Text("Essa ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarExclusao = false
                    viewModel.excluir(alerta)
                    onVoltar()
                }) { Text("Excluir") }
            },
            dismissButton = {
                TextButton(onClick = { confirmarExclusao = false }) { Text("Cancelar") }
            }
        )
    }
}
