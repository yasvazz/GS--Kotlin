package br.com.fiap.spate.model

/** Nível de risco de um alerta, do menos para o mais grave. */
enum class NivelRisco(val rotulo: String) {
    BAIXO("Baixo"),
    MODERADO("Moderado"),
    ALTO("Alto"),
    CRITICO("Crítico")
}
