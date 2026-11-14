package br.com.fiap.spate.navigation

/** Endereços das telas do app. Os que têm {id} recebem o id do alerta. */
object Rotas {
    const val HOME = "home"
    const val LISTA = "lista"
    const val FORM_NOVO = "form"
    const val FORM_EDITAR = "form/{id}"
    const val DETALHE = "detalhe/{id}"

    const val ARG_ID = "id"

    fun formEditar(id: Int) = "form/$id"
    fun detalhe(id: Int) = "detalhe/$id"
}
