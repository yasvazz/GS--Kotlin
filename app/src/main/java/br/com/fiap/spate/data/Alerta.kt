package br.com.fiap.spate.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.fiap.spate.model.NivelRisco
import br.com.fiap.spate.model.TipoAlerta

/**
 * Entidade principal do app: um alerta de risco registrado para um bairro.
 * Cada objeto desta classe vira uma linha na tabela "alertas" do Room.
 *
 * "tipo" e "nivelRisco" guardam o NOME do enum (ex.: "ENCHENTE"),
 * assim o banco não precisa de conversores.
 */
@Entity(tableName = "alertas")
data class Alerta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tipo: String,
    val bairro: String,
    val descricao: String,
    val nivelRisco: String,
    val resolvido: Boolean = false,
    val favorito: Boolean = false,
    val dataCriacao: Long = System.currentTimeMillis()
)

/** Converte o texto salvo no banco de volta para o enum (com valor padrão se vier algo inesperado). */
fun Alerta.tipoEnum(): TipoAlerta =
    runCatching { TipoAlerta.valueOf(tipo) }.getOrDefault(TipoAlerta.ENCHENTE)

fun Alerta.nivelEnum(): NivelRisco =
    runCatching { NivelRisco.valueOf(nivelRisco) }.getOrDefault(NivelRisco.MODERADO)
