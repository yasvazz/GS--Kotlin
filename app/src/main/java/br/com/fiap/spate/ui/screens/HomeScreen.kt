package br.com.fiap.spate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.fiap.spate.model.NivelRisco
import br.com.fiap.spate.viewmodel.AlertaViewModel

/** Tela inicial: apresenta o app, mostra um resumo e dá acesso às funcionalidades. */
@Composable
fun HomeScreen(
    viewModel: AlertaViewModel,
    onVerAlertas: () -> Unit,
    onNovoAlerta: () -> Unit
) {
    val alertas by viewModel.alertas.collectAsState()
    val lista = alertas.orEmpty()
    val ativos = lista.count { !it.resolvido }
    val criticos = lista.count { !it.resolvido && it.nivelRisco == NivelRisco.CRITICO.name }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cabeçalho com identidade visual
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("🛡️ Spate", style = MaterialTheme.typography.headlineLarge)
                    Text(
                        text = "Alertas de enchentes, deslizamentos, incêndios e ondas de calor " +
                            "direcionados à população.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Resumo (calculado a partir dos dados salvos no Room)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ResumoCard("Alertas ativos", ativos.toString(), Modifier.weight(1f))
                ResumoCard("Nível crítico", criticos.toString(), Modifier.weight(1f))
            }

            Button(onClick = onVerAlertas, modifier = Modifier.fillMaxWidth()) {
                Text("Ver alertas")
            }
            OutlinedButton(onClick = onNovoAlerta, modifier = Modifier.fillMaxWidth()) {
                Text("Registrar novo alerta")
            }

            // Contexto da solução
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Para quem é?", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Moradores de áreas de risco e agentes comunitários, que precisam " +
                            "saber rápido o que está acontecendo no seu bairro.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "ODS 11 • ODS 13 • ODS 3",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Text(
                text = "Protótipo acadêmico: os dados são cadastrados no próprio aparelho " +
                    "e não há integração com órgãos oficiais.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ResumoCard(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(valor, style = MaterialTheme.typography.displaySmall)
            Text(titulo, style = MaterialTheme.typography.labelMedium)
        }
    }
}
