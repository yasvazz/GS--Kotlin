package br.com.fiap.spate.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository: camada entre o ViewModel e o banco.
 * O ViewModel não precisa saber que os dados vêm do Room.
 */
class AlertaRepository(private val dao: AlertaDao) {

    val alertas: Flow<List<Alerta>> = dao.observarTodos()

    suspend fun buscarPorId(id: Int): Alerta? = dao.buscarPorId(id)

    suspend fun inserir(alerta: Alerta) = dao.inserir(alerta)

    suspend fun atualizar(alerta: Alerta) = dao.atualizar(alerta)

    suspend fun excluir(alerta: Alerta) = dao.excluir(alerta)
}
