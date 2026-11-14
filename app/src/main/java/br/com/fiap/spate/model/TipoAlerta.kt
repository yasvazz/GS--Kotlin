package br.com.fiap.spate.model

/**
 * Tipos de risco que o Spate monitora.
 * "rotulo" é o texto mostrado ao usuário; "icone" é um emoji usado nos cards.
 */
enum class TipoAlerta(val rotulo: String, val icone: String) {
    ENCHENTE("Enchente", "🌊"),
    DESLIZAMENTO("Deslizamento", "⛰️"),
    INCENDIO("Incêndio", "🔥"),
    ONDA_DE_CALOR("Onda de calor", "🌡️")
}
