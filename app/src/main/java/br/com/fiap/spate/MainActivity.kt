package br.com.fiap.spate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import br.com.fiap.spate.navigation.SpateNavHost
import br.com.fiap.spate.ui.theme.SpateTheme
import br.com.fiap.spate.viewmodel.AlertaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpateTheme {
                SpateApp()
            }
        }
    }
}

/** Raiz do app: cria a navegação, o ViewModel e mostra as mensagens (Snackbar). */
@Composable
fun SpateApp() {
    val navController = rememberNavController()
    val viewModel: AlertaViewModel = viewModel()
    val snackbarHostState = remember { SnackbarHostState() }

    // Toda mensagem emitida pelo ViewModel aparece como Snackbar
    LaunchedEffect(Unit) {
        viewModel.mensagens.collect { snackbarHostState.showSnackbar(it) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SpateNavHost(navController = navController, viewModel = viewModel)
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 80.dp)
        )
    }
}
