@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.fiap.spate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.fiap.spate.model.TipoAlerta
import br.com.fiap.spate.ui.components.AlertaCard
import br.com.fiap.spate.ui.components.EstadoVazio
import br.com.fiap.spate.viewmodel.AlertaViewModel

/** Tela de listagem/acompanhamento dos alertas, com filtro por tipo de risco. */
@Composable
fun ListaScreen(
    viewModel: AlertaViewModel,
    onVoltar: () -> Unit,
    onNovo: () -> Unit,
    onAbrir: (Int) -> Unit
) {
    val alertas by viewModel.alertasFiltrados.collectAsState()
    val filtro by viewModel.filtro.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alertas") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNovo) {
                Icon(Icons.Filled.Add, contentDescription = "Registrar alerta")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = filtro == null,
                        onClick = { viewModel.definirFiltro(null) },
                        label = { Text("Todos") }
                    )
                }
                items(TipoAlerta.entries) { tipo ->
                    FilterChip(
                        selected = filtro == tipo,
                        onClick = { viewModel.definirFiltro(tipo) },
                        label = { Text("${tipo.icone} ${tipo.rotulo}") }
                    )
                }
            }

            val lista = alertas
            when {
                // Ainda carregando do banco
                lista == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

                // Estado sem dados
                lista.isEmpty() -> {
                    if (filtro == null) {
                        EstadoVazio(
                            titulo = "Nenhum alerta registrado",
                            mensagem = "Registre o primeiro alerta do seu bairro ou carregue exemplos para testar."
                        ) {
                            Button(onClick = viewModel::inserirExemplos) {
                                Text("Carregar alertas de exemplo")
                            }
                        }
                    } else {
                        EstadoVazio(
                            titulo = "Nada por aqui",
                            mensagem = "Não há alertas deste tipo. Escolha outro filtro ou registre um novo alerta."
                        )
                    }
                }

                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 88.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(lista, key = { it.id }) { alerta ->
                        AlertaCard(
                            alerta = alerta,
                            onClick = { onAbrir(alerta.id) },
                            onAlternarResolvido = { viewModel.alternarResolvido(alerta) },
                            onAlternarFavorito = { viewModel.alternarFavorito(alerta) }
                        )
                    }
                }
            }
        }
    }
}
