package br.com.fiap.spate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.fiap.spate.ui.screens.DetalheScreen
import br.com.fiap.spate.ui.screens.FormScreen
import br.com.fiap.spate.ui.screens.HomeScreen
import br.com.fiap.spate.ui.screens.ListaScreen
import br.com.fiap.spate.viewmodel.AlertaViewModel

/** Define quais telas existem e como ir de uma para outra. */
@Composable
fun SpateNavHost(navController: NavHostController, viewModel: AlertaViewModel) {
    val argId = listOf(navArgument(Rotas.ARG_ID) { type = NavType.IntType })

    NavHost(navController = navController, startDestination = Rotas.HOME) {

        composable(Rotas.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onVerAlertas = { navController.navigate(Rotas.LISTA) },
                onNovoAlerta = { navController.navigate(Rotas.FORM_NOVO) }
            )
        }

        composable(Rotas.LISTA) {
            ListaScreen(
                viewModel = viewModel,
                onVoltar = { navController.popBackStack() },
                onNovo = { navController.navigate(Rotas.FORM_NOVO) },
                onAbrir = { id -> navController.navigate(Rotas.detalhe(id)) }
            )
        }

        composable(Rotas.FORM_NOVO) {
            FormScreen(
                viewModel = viewModel,
                alertaId = null,
                onVoltar = { navController.popBackStack() },
                // Depois de registrar, mostra a lista (com o alerta novo no topo)
                onSalvo = {
                    navController.navigate(Rotas.LISTA) {
                        popUpTo(Rotas.HOME)
                    }
                }
            )
        }

        composable(Rotas.FORM_EDITAR, arguments = argId) { entry ->
            FormScreen(
                viewModel = viewModel,
                alertaId = entry.arguments?.getInt(Rotas.ARG_ID),
                onVoltar = { navController.popBackStack() },
                // Depois de editar, volta para os detalhes do alerta
                onSalvo = { navController.popBackStack() }
            )
        }

        composable(Rotas.DETALHE, arguments = argId) { entry ->
            val id = entry.arguments?.getInt(Rotas.ARG_ID) ?: 0
            DetalheScreen(
                viewModel = viewModel,
                alertaId = id,
                onVoltar = { navController.popBackStack() },
                onEditar = { navController.navigate(Rotas.formEditar(id)) }
            )
        }
    }
}
