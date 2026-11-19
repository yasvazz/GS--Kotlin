@file:OptIn(ExperimentalMaterial3Api::class)
package br.com.fiap.spate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.fiap.spate.model.NivelRisco
import br.com.fiap.spate.model.TipoAlerta
import br.com.fiap.spate.viewmodel.AlertaViewModel

/** Tela de cadastro (alertaId = null) e de edição (alertaId = id do alerta). */
@Composable
fun FormScreen(
    viewModel: AlertaViewModel,
    alertaId: Int?,
    onVoltar: () -> Unit,
    onSalvo: () -> Unit
) {
    // Ao abrir a tela: limpa o formulário (novo) ou carrega o alerta (edição)
    LaunchedEffect(alertaId) {
        if (alertaId == null) viewModel.novoFormulario() else viewModel.editarFormulario(alertaId)
    }

    val form by viewModel.form.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (form.editando) "Editar alerta" else "Novo alerta") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Tipo de risco", style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TipoAlerta.entries.forEach { tipo ->
                    FilterChip(
                        selected = form.tipo == tipo,
                        onClick = { viewModel.onTipoChange(tipo) },
                        label = { Text("${tipo.icone} ${tipo.rotulo}") }
                    )
                }
            }

            OutlinedTextField(
                value = form.bairro,
                onValueChange = viewModel::onBairroChange,
                label = { Text("Bairro / local *") },
                singleLine = true,
                isError = form.erroBairro != null,
                supportingText = { form.erroBairro?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = form.descricao,
                onValueChange = viewModel::onDescricaoChange,
                label = { Text("Descrição da situação *") },
                minLines = 3,
                isError = form.erroDescricao != null,
                supportingText = { form.erroDescricao?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Nível de risco", style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NivelRisco.entries.forEach { nivel ->
                    FilterChip(
                        selected = form.nivel == nivel,
                        onClick = { viewModel.onNivelChange(nivel) },
                        label = { Text(nivel.rotulo) }
                    )
                }
            }

            Button(
                onClick = { viewModel.salvar(onSucesso = onSalvo) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (form.editando) "Salvar alterações" else "Registrar alerta")
            }
            OutlinedButton(onClick = onVoltar, modifier = Modifier.fillMaxWidth()) {
                Text("Cancelar")
            }
        }
    }
}