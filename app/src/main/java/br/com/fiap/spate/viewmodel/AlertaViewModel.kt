package br.com.fiap.spate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.spate.data.Alerta
import br.com.fiap.spate.data.AlertaRepository
import br.com.fiap.spate.data.SpateDatabase
import br.com.fiap.spate.data.nivelEnum
import br.com.fiap.spate.data.tipoEnum
import br.com.fiap.spate.model.NivelRisco
import br.com.fiap.spate.model.TipoAlerta
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Estado do formulário de cadastro/edição (inclui as mensagens de erro de validação). */
data class FormState(
    val id: Int = 0,
    val tipo: TipoAlerta = TipoAlerta.ENCHENTE,
    val bairro: String = "",
    val descricao: String = "",
    val nivel: NivelRisco = NivelRisco.MODERADO,
    val resolvido: Boolean = false,
    val favorito: Boolean = false,
    val dataCriacao: Long = 0L,
    val erroBairro: String? = null,
    val erroDescricao: String? = null
) {
    val editando: Boolean get() = id != 0
}

/**
 * ViewModel único do app. Guarda o estado das telas e conversa com o Repository.
 * Sobrevive a rotações de tela, então o estado não se perde.
 */
class AlertaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
        AlertaRepository(SpateDatabase.getInstance(application).alertaDao())

    // ---------- Lista ----------

    private val _filtro = MutableStateFlow<TipoAlerta?>(null)
    val filtro: StateFlow<TipoAlerta?> = _filtro.asStateFlow()

    /** null = ainda carregando do banco; lista vazia = banco sem registros. */
    val alertas: StateFlow<List<Alerta>?> = repository.alertas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    /** Lista já filtrada pelo tipo escolhido na tela de listagem. */
    val alertasFiltrados: StateFlow<List<Alerta>?> = combine(alertas, _filtro) { lista, tipo ->
        if (tipo == null) lista else lista?.filter { it.tipoEnum() == tipo }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun definirFiltro(tipo: TipoAlerta?) {
        _filtro.value = tipo
    }

    // ---------- Mensagens para o usuário (Snackbar) ----------

    private val _mensagens = MutableSharedFlow<String>(extraBufferCapacity = 4)
    val mensagens: SharedFlow<String> = _mensagens.asSharedFlow()

    // ---------- Formulário ----------

    private val _form = MutableStateFlow(FormState())
    val form: StateFlow<FormState> = _form.asStateFlow()

    fun novoFormulario() {
        _form.value = FormState()
    }

    fun editarFormulario(id: Int) {
        viewModelScope.launch {
            repository.buscarPorId(id)?.let { a ->
                _form.value = FormState(
                    id = a.id,
                    tipo = a.tipoEnum(),
                    bairro = a.bairro,
                    descricao = a.descricao,
                    nivel = a.nivelEnum(),
                    resolvido = a.resolvido,
                    favorito = a.favorito,
                    dataCriacao = a.dataCriacao
                )
            }
        }
    }

    fun onTipoChange(tipo: TipoAlerta) = _form.update { it.copy(tipo = tipo) }

    fun onNivelChange(nivel: NivelRisco) = _form.update { it.copy(nivel = nivel) }

    fun onBairroChange(texto: String) =
        _form.update { it.copy(bairro = texto, erroBairro = null) }

    fun onDescricaoChange(texto: String) =
        _form.update { it.copy(descricao = texto, erroDescricao = null) }

    /**
     * Valida os campos obrigatórios e salva no Room.
     * Só chama [onSucesso] (voltar de tela) se tudo estiver válido.
     */
    fun salvar(onSucesso: () -> Unit) {
        val f = _form.value

        val erroBairro =
            if (f.bairro.trim().length < 3) "Informe o bairro (mínimo 3 letras)" else null
        val erroDescricao =
            if (f.descricao.trim().length < 10) "Descreva a situação (mínimo 10 caracteres)" else null

        if (erroBairro != null || erroDescricao != null) {
            _form.update { it.copy(erroBairro = erroBairro, erroDescricao = erroDescricao) }
            return
        }

        viewModelScope.launch {
            val alerta = Alerta(
                id = f.id,
                tipo = f.tipo.name,
                bairro = f.bairro.trim(),
                descricao = f.descricao.trim(),
                nivelRisco = f.nivel.name,
                resolvido = f.resolvido,
                favorito = f.favorito,
                dataCriacao = if (f.dataCriacao == 0L) System.currentTimeMillis() else f.dataCriacao
            )
            if (f.editando) {
                repository.atualizar(alerta)
                _mensagens.tryEmit("Alerta atualizado")
            } else {
                repository.inserir(alerta)
                _mensagens.tryEmit("Alerta registrado")
            }
            onSucesso()
        }
    }

    // ---------- Ações sobre os registros ----------

    fun alternarResolvido(alerta: Alerta) {
        viewModelScope.launch {
            repository.atualizar(alerta.copy(resolvido = !alerta.resolvido))
            _mensagens.tryEmit(
                if (alerta.resolvido) "Alerta reativado" else "Alerta marcado como resolvido"
            )
        }
    }

    fun alternarFavorito(alerta: Alerta) {
        viewModelScope.launch {
            repository.atualizar(alerta.copy(favorito = !alerta.favorito))
        }
    }

    fun excluir(alerta: Alerta) {
        viewModelScope.launch {
            repository.excluir(alerta)
            _mensagens.tryEmit("Alerta excluído")
        }
    }

    /** Dados simulados para demonstração (o app é um protótipo acadêmico). */
    fun inserirExemplos() {
        viewModelScope.launch {
            val agora = System.currentTimeMillis()
            val hora = 3_600_000L
            listOf(
                Alerta(
                    tipo = TipoAlerta.ENCHENTE.name,
                    bairro = "Jardim Pantanal",
                    descricao = "Córrego transbordando após chuva forte. Ruas alagadas perto da ponte.",
                    nivelRisco = NivelRisco.ALTO.name,
                    dataCriacao = agora - 2 * hora
                ),
                Alerta(
                    tipo = TipoAlerta.DESLIZAMENTO.name,
                    bairro = "Morro do Alemão",
                    descricao = "Rachaduras na encosta e terra solta perto das casas da parte alta.",
                    nivelRisco = NivelRisco.CRITICO.name,
                    dataCriacao = agora - 5 * hora
                ),
                Alerta(
                    tipo = TipoAlerta.INCENDIO.name,
                    bairro = "Vila Esperança",
                    descricao = "Foco de queimada em terreno baldio com vegetação seca.",
                    nivelRisco = NivelRisco.MODERADO.name,
                    dataCriacao = agora - 8 * hora
                ),
                Alerta(
                    tipo = TipoAlerta.ONDA_DE_CALOR.name,
                    bairro = "Centro",
                    descricao = "Temperatura acima de 38 °C prevista para os próximos 3 dias.",
                    nivelRisco = NivelRisco.BAIXO.name,
                    dataCriacao = agora - 24 * hora
                )
            ).forEach { repository.inserir(it) }
            _mensagens.tryEmit("Alertas de exemplo carregados")
        }
    }
}
